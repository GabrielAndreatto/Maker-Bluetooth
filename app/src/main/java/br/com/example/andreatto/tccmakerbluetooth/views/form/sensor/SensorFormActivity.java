package br.com.example.andreatto.tccmakerbluetooth.views.form.sensor;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.IconsAppDAO;
import br.com.example.andreatto.tccmakerbluetooth.dao.SensorDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.IconsApp;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Sensor;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.classes.Print;

public class SensorFormActivity extends AppCompatActivity {

    private Toolbar toolbarSensor;
    private ImageView imageViewSensorIcon;
    private EditText editTextName, editTextCommand;
    private Button save, cancel;

    Bundle pacote;
    String code;
    String URI_ICON = "@drawable/";

    private int sensorId;
    private Sensor sensor;
    private SensorDAO sensorDAO;
    private List<Sensor> sensorList;

    private String inconIdView = "0";
    private Print print = new Print();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_sensor);

        pacote = getIntent().getExtras();
        if(!pacote.isEmpty() || getIntent().getExtras() != null) {

            code = pacote.getString("code");
            sensorList = new ArrayList<>();
            sensorDAO = new SensorDAO(this);

            if (code.contains("new")) {
                print.logE("CODE", code);

                sensor = new Sensor();
                sensor.setIcon(inconIdView);
            }
            if (code.contains("edit")) {
                print.logE("CODE", code);

                sensorId = pacote.getInt("sensor_id");
                sensor = getSensor();
                inconIdView = sensor.getIcon();
            }
        }

        print.logE("SENSOR: ", " "+sensor.getName());
        initialUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUIValuesInitilized();
        initialUIlistener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initialUI();
    }

    private Sensor getSensor() {
        sensorList = sensorDAO.all();
        Sensor sensorT = new Sensor();
        for (Sensor tmpSensor: sensorList) {
            if(tmpSensor.getId() == sensorId) {
                sensorT = tmpSensor;
            }
        }
        return sensorT;
    }

    private void initialUI() {
        editTextName = findViewById(R.id.sensor_name);
        editTextCommand = findViewById(R.id.sensor_cmd);
        save = findViewById(R.id.btn_save);
        cancel = findViewById(R.id.btn_cancel);
        imageViewSensorIcon = findViewById(R.id.imageView_sensor_icon);

        toolbarSensor = findViewById(R.id.toolbar_board);
        if (code.contains("new")) {
            toolbarSensor.setTitle("Sensor ");
        } else {
            toolbarSensor.setTitle("Sensor " + sensor.getName());
        }
        setSupportActionBar(toolbarSensor);
    }

    private void initialUIlistener() {

        imageViewSensorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("icon_base", inconIdView);
                Intent i = new Intent(getApplicationContext(), IconsSensorActivity.class);
                i.putExtras(b);
                startActivityForResult(i, 343);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                initialObject();
                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        try{
                            sensorDAO.save(sensor);
                            if(pacote.getString("code").contains("edit")) {
                                print.toast(getApplicationContext(), "Sensor editado com sucesso", true);
                            } else {
                                print.toast(getApplicationContext(), "Sensor criado com sucesso", true);
                            }
                            returnActivityOn();

                        } catch (Exception e) {
                            printObjectCRUD("cadastrar", false);
                            Log.e("ERROR", "SENSOR ADD " + e.getMessage());
                        }
                    }
                });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInputsUI();
            }
        });
    }

    private IconsApp getIconApp(String id) {
        IconsAppDAO iconsAppDAOT = new IconsAppDAO(getApplicationContext());
        List<IconsApp> iconsAppListT = iconsAppDAOT.all();
        IconsApp iconsAppT = iconsAppListT.get(Integer.parseInt(id));
        return  iconsAppT;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == 343) {
            if(intent.getExtras() != null) {

                final int iconSelectedT = intent.getExtras().getInt("icon");
                inconIdView = String.valueOf(iconSelectedT);

                print.logE("URI_T_Result", "idRet: "+inconIdView);

                sensor.setIcon(inconIdView);
                final IconsApp iconsAppT = getIconApp(sensor.getIcon());
                print.logE("sensor_icon", "getIcon RET: "+sensor.getIcon());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String uriT = URI_ICON + iconsAppT.getIcon();

                        print.logE("URITResult", uriT);

                        int imageResource = getResources().getIdentifier(uriT, null, getPackageName());
                        final Drawable res = getResources().getDrawable(imageResource);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageViewSensorIcon.setImageDrawable(res);
                            }
                        });
                    }
                });
            }
        }
    }

    public void returnActivityOn() {
        Bundle pkg = new Bundle();
        pkg.putString("action", pacote.getString("code"));
        Intent returnIntent = new Intent();
        returnIntent.putExtras(pkg);
        setResult(200, returnIntent);
        finish();
    }

    private void setUIValuesInitilized() {
        editTextName.setText(this.sensor.getName());
        editTextCommand.setText(this.sensor.getCommand());
        setUiIconInitialized();
    }

    private void setUiIconInitialized() {
        IconsApp iconsAppT = getIconApp(sensor.getIcon());
        String uriT = URI_ICON + iconsAppT.getIcon();
        int imageResource = getResources().getIdentifier(uriT, null, getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        imageViewSensorIcon.setImageDrawable(res);
    }

    private void initialObject() {
        sensor.setName(editTextName.getText().toString());
        sensor.setCommand(editTextCommand.getText().toString());
    }

    private void clearInputsUI() {
        editTextName.setText("");
        editTextCommand.setText("");
    }

    void printObjectCRUD(String msg, Boolean condition) {
        if(condition) {
            Toast.makeText(getApplicationContext(), "Sucesso ao "+msg, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Erro ao "+msg, Toast.LENGTH_LONG).show();
        }
    }

}
