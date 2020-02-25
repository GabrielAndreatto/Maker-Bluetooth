package br.com.example.andreatto.tccmakerbluetooth.views.listas.sensors;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.IconsAppDAO;
import br.com.example.andreatto.tccmakerbluetooth.dao.SensorDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.IconsApp;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Sensor;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceConnectionBluetoothBind;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.activitys.AppCompatActivityBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.classes.AgileTools;
import br.com.example.andreatto.tccmakerbluetooth.views.form.sensor.SensorFormActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.ui.SensorValue;

public class RecyclerSensorListAdapter extends RecyclerView.Adapter<ViewHolderSensor> {

    private final String TAG = "SENSOR";
    private List<Sensor> sensors;
    private ImageView iconSensor;
    private ImageView iconSensorEdit;
    private ImageView iconSensorDelete  ;

    Activity activity;
    private AgileTools tools;

    private Intent intentService;
    private ServiceConnectionBluetoothBind serviceConnection;

    private String hora = "";
    private String dia = "";

    public RecyclerSensorListAdapter(List<Sensor> sensors, Activity activity) {
        this.sensors = sensors;
        this.activity = activity;

        intentService = new Intent(activity, ServiceBluetooth.class);
        serviceConnection = new ServiceConnectionBluetoothBind();
        getActivity().getApplicationContext().bindService(intentService, serviceConnection, 0);
    }

    @NonNull
    @Override
    public ViewHolderSensor onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_sensor, viewGroup, false);
        return new ViewHolderSensor(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSensor viewHolderSensor, final int position) {
        final Sensor sensorT = sensors.get(position);

        if(sensorT.getName() != null) {
            viewHolderSensor.name.setText(sensorT.getName());
        } else {
            viewHolderSensor.name.setText("");
        }
        if(sensorT.getCommand() != null) {
            viewHolderSensor.command.setText(sensorT.getCommand());
        } else {
            viewHolderSensor.command.setText("");
        }
        if(sensorT.getValue() != null) {
            viewHolderSensor.value.setText(sensorT.getValue());
        } else {
            viewHolderSensor.value.setText("");
        }
        if(sensorT.getUsed_at() != null) {
            String[] dataValue = getSplit(sensorT.getUsed_at(), " ");
            try {
                dia = dataValue[0];
                hora = getSubString(dataValue[1], 0, 8);
            } catch (Exception e) {
                hora = "";
                dia = "";
            }

            viewHolderSensor.used_hour.setText(hora);
            viewHolderSensor.used_day.setText(dia);
        }
        if(sensorT.getIcon() != null) {
            IconsAppDAO iconsAppDAOT = new IconsAppDAO(activity);
            List<IconsApp> iconsAppListT = iconsAppDAOT.all();
            IconsApp iconsAppT = iconsAppListT.get(Integer.parseInt(sensorT.getIcon()));

            iconSensor = viewHolderSensor.itemView.findViewById(R.id.btn_sensor_main);
            String uri = "@drawable/" + iconsAppT.getIcon();
            int imageResource = activity.getResources().getIdentifier(uri, null,  activity.getPackageName());
            Drawable res = activity.getResources().getDrawable(imageResource);
            iconSensor.setImageDrawable(res);
        } else {
            iconSensor = viewHolderSensor.itemView.findViewById(R.id.btn_sensor_main);
            String uri = "@drawable/ic_sensor_light";
            int imageResource = activity.getResources().getIdentifier(uri, null,  activity.getPackageName());
            Drawable res = activity.getResources().getDrawable(imageResource);
            iconSensor.setImageDrawable(res);
        }
        iconSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        Bundle pkg = new Bundle();
                        pkg.putString("code", "getValue");
                        pkg.putInt("sensor_id", (int) sensorT.getId());

                        Intent i = new Intent(v.getContext().getApplicationContext(), SensorValue.class);
                        i.putExtras(pkg);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        v.getContext().getApplicationContext().startActivity(i);
                    }
                });
            }
        });

        iconSensorEdit = viewHolderSensor.itemView.findViewById(R.id.imageView_edit);
        iconSensorEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        Bundle pkg = new Bundle();
                        pkg.putString("code", "edit");
                        pkg.putInt("sensor_id", (int) sensorT.getId());

                        Intent i = new Intent(v.getContext().getApplicationContext(), SensorFormActivity.class);
                        i.putExtras(pkg);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        v.getContext().getApplicationContext().startActivity(i);
                    }
                });
            }
        });

        iconSensorDelete = viewHolderSensor.itemView.findViewById(R.id.imageView_delete);
        iconSensorDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.post(new Runnable() {
                    @Override
                    public void run() {

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                                final ViewGroup viewGroup = activity.findViewById(android.R.id.content);

                                //then we will inflate the custom alert dialog xml that we created
                                final View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_confirm_sensor, viewGroup, false);

                                //Now we need an AlertDialog.Builder object
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                                //setting the view of the builder to our custom view that we already inflated
                                builder.setView(dialogView);

                                //finally creating the alert dialog and displaying it
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();

                                dialogView.findViewById(R.id.sensorButtonOk).setOnClickListener(new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(View v) {
                                        SensorDAO sensorDAO = new SensorDAO(v.getContext().getApplicationContext());
                                        sensorDAO.remover(sensorT);

                                        Log.e("iconSensorDelete", "iconSensorDelete: "+sensorT.getId());

                                        try {
                                            Thread.sleep(100);
                                            activity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, sensors.size());
                                                }
                                            });
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            Thread.sleep(200);
                                            activity.runOnUiThread(new Runnable() {
                                                @RequiresApi(api = Build.VERSION_CODES.M)
                                                @Override
                                                public void run() {
                                                    showMessageSnack(viewGroup, sensorT.getName());
                                                }
                                            });
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            Intent intent = new Intent(activity, AppCompatActivityBluetooth.class);
                                            intent.putExtra("refresh", "ok");

                                            Thread.sleep(150);
                                            activity.startActivityForResult(intent, 201);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        alertDialog.cancel();
                                    }
                                });

                                dialogView.findViewById(R.id.sensorButtonCancel).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.cancel();
                                    }

                                });

                            }
                        });


                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showMessageSnack(View v, String paramNomeSensor) {
        final Snackbar snackbar = Snackbar.make(v, "O Sensor " + paramNomeSensor + " foi removido com sucesso", Snackbar.LENGTH_LONG);

        View snackView = snackbar.getView();
        snackView.setBackgroundColor(v.getResources().getColor(R.color.secondaryTextColor));

        TextView snackActionView  = snackView.findViewById(android.support.design.R.id.snackbar_action);
        snackActionView.setTextColor(v.getResources().getColor(android.R.color.white, null));

        snackbar.show();

    }

    @Override
    public int getItemCount() {
        return sensors.size();
    }

    private Activity getActivity() {
        return this.activity;
    }

    public String[] getSplit(String msg, String condition) {
        String[] s = new String[0];
        try {
            s = msg.split(condition);
        } catch (Exception e) {
            s[0] = e.getMessage();
        }
        return s;
    }

    public String getSubString(String msg, int first, int last) {
        String s = "";
        try {
            s = msg.substring(first, last);
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
        return s;
    }

    public String getSubStringStart(String msg, int first) {
        String s = "";
        try {
            s = msg.substring(first);
        } catch (Exception e){
            Log.e("Exception: ", e.getMessage());
        }
        return s;
    }

}
