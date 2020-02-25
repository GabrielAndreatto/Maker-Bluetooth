package br.com.example.andreatto.tccmakerbluetooth.views.listas.actuators.adapterOnOff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.ActuactorDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Actuator;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceConnectionBluetoothBind;
import br.com.example.andreatto.tccmakerbluetooth.views.form.actuator.ActuatorFormActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.actuators.api.DeleteActuatorActivity;

public class ActuatorsOnoffListAdapterTab extends RecyclerView.Adapter<ViewHolderActuatorOnoffTab>  {

    private final String TAG = "ACTUATORS";
    private List<Actuator> actuators;
    private ActuactorDAO actuactorDAO;

    Activity activity;
    SwitchCompat btnSwitchCompat;
    ImageView imageViewEdit, imageViewDelete;

    // SERVICE
    private Intent intentService;
    private ServiceConnectionBluetoothBind serviceConnection;

    public ActuatorsOnoffListAdapterTab(List<Actuator> actuatorsT, Activity activity) {
        this.actuators = actuatorsT;
        this.activity = activity;

        actuactorDAO = new ActuactorDAO(getActivity());

        intentService = new Intent(activity, ServiceBluetooth.class);
        serviceConnection = new ServiceConnectionBluetoothBind();
        getActivity().getApplicationContext().bindService(intentService, serviceConnection, 0);
    }

    private Context getActivity() {
        return this.activity;
    }

    @NonNull
    @Override
    public ViewHolderActuatorOnoffTab onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.actuator_switch, viewGroup, false);

        return new ViewHolderActuatorOnoffTab(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolderActuatorOnoffTab viewHolderActuatorOnoffTab, final int position) {
        final Actuator actuatorT = actuators.get(position);

        Log.e("LIST", "n: "+actuators.size());
        Log.e("ATUADOR", "Name: "+actuatorT.getName());
        Log.e("TIPO", "Tipo: "+actuatorT.getTipo());

        viewHolderActuatorOnoffTab.nameOnoff.setText(actuatorT.getName());
        viewHolderActuatorOnoffTab.commandOn.setText(actuatorT.getCommandOn());
        viewHolderActuatorOnoffTab.commandOff.setText(actuatorT.getCommandOff());

        btnSwitchCompat = viewHolderActuatorOnoffTab.itemView.findViewById(R.id.switch_actuator_onoff);
        if(actuatorT.getActived() != 0) {
            btnSwitchCompat.setChecked(true);
        } else {
            btnSwitchCompat.setChecked(false);
        }
        btnSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                actuatorT.setActived(1);
                                actuactorDAO.updateCommand(actuatorT);
                            }catch (Exception e) {

                            }
                        }
                    }).start();
                    serviceConnection.getServiceBluetooth().sendCommand(actuatorT.getCommandOn());
                }
                if(!isChecked){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                actuatorT.setActived(0);
                                actuactorDAO.updateCommand(actuatorT);
                            } catch (Exception e) {

                            }
                        }
                    }).start();
                    serviceConnection.getServiceBluetooth().sendCommand(actuatorT.getCommandOff());
                }
            }
        });

        imageViewEdit = viewHolderActuatorOnoffTab.itemView.findViewById(R.id.imageView_editar);
        imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.post(new Runnable() {
                    @Override
                    public void run() {

                        Bundle pkg = new Bundle();
                        pkg.putString("code", "edit");
                        pkg.putInt("actuator_id", (int) actuatorT.getId());

                        Intent i = new Intent(v.getContext().getApplicationContext(), ActuatorFormActivity.class);
                        i.putExtras(pkg);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        v.getContext().getApplicationContext().startActivity(i);

                    }
                });
            }
        });

        imageViewDelete = viewHolderActuatorOnoffTab.itemView.findViewById(R.id.imageView_deletar);
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //showCustomDialog(board, position);

                        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                        final ViewGroup viewGroup = activity.findViewById(android.R.id.content);

                        //then we will inflate the custom alert dialog xml that we created
                        final View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_confirm_board, viewGroup, false);

                        //Now we need an AlertDialog.Builder object
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        //setting the view of the builder to our custom view that we already inflated
                        builder.setView(dialogView);

                        //finally creating the alert dialog and displaying it
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        dialogView.findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(View v) {

                                try {

                                    // chamar activity para deletar atuador
                                    Bundle pkg = new Bundle();
                                    pkg.putString("code", "delete");
                                    pkg.putInt("actuator_id", (int) actuatorT.getId());

                                    Intent i = new Intent(v.getContext().getApplicationContext(), DeleteActuatorActivity.class);
                                    i.putExtras(pkg);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    v.getContext().getApplicationContext().startActivity(i);

                                    Log.e("btnDelete", "btnDelete Remover actuador: "+actuatorT.getId());

                                } catch (Exception e) {
                                    Log.e("REMOVE", "ERROR Actuador: ");
                                } finally {

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, actuators.size());
                                        }
                                    });

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showMessageSnack(viewGroup, actuatorT.getName());
                                        }
                                    });

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            alertDialog.cancel();
                                        }
                                    });

                                }
                            }
                        });

                        dialogView.findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showMessageSnack(View v, String paramNomeBoard) {
        final Snackbar snackbar = Snackbar.make(v, "A Board " + paramNomeBoard + " foi removida com sucesso", Snackbar.LENGTH_LONG);

        View snackView = snackbar.getView();
        snackView.setBackgroundColor(v.getResources().getColor(R.color.secondaryTextColor));

        TextView snackActionView  = snackView.findViewById(android.support.design.R.id.snackbar_action);
        snackActionView.setTextColor(v.getResources().getColor(android.R.color.white, null));

        snackbar.show();

    }

    @Override
    public int getItemCount() {
        return actuators.size();
    }

}
