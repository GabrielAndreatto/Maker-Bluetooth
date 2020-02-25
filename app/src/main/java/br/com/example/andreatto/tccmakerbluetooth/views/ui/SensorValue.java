package br.com.example.andreatto.tccmakerbluetooth.views.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.IconsAppDAO;
import br.com.example.andreatto.tccmakerbluetooth.dao.SensorDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.IconsApp;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Sensor;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceConnectionBluetoothBind;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.classes.AgileTools;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.classes.Print;

public class SensorValue extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbarSensorValues;

    private ImageView iconSensor, imageViewIcon;
    private LinearLayoutCompat layout;
    private TextView textViewNomeSensor;
    private TextView textViewSensorValue;
    private TextView textViewSensorCommand;
    private TextView textViewSensorDate;
    private FloatingActionButton fab;

    private int sensorId;
    private Sensor sensor;
    private SensorDAO sensorDAO = new SensorDAO(this);
    private List<Sensor> sensorList = new ArrayList<Sensor>();
    private Bundle pacote;

    // SERVICE Bluetooth
    private Intent intentService;
    private ServiceConnectionBluetoothBind serviceConnection;

    // Preferences
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor sharedPreferenceEditor;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    Print print = new Print();

    private AgileTools tools = new AgileTools();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_value);

        pacote = getIntent().getExtras();
        if(!pacote.isEmpty() || getIntent().getExtras() != null) {
            if (pacote.getString("code").contains("getValue")) {
                sensorId = pacote.getInt("sensor_id");
                sensor = getSensor();
            }
        }
        initialUI();
        initialUIListeners();
        initialSharedPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUIValuesInitilized();
        bindService();
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

    private void getSensorValue() {
        sensor.setValue(textViewSensorValue.getText().toString());
        sensor.setUsed_at(textViewSensorDate.getText().toString());
    }

    private void initialUI() {
        //getSupportActionBar().setTitle("Sensor Value");
        iconSensor = findViewById(R.id.imageView_sensor_value);
        imageViewIcon = findViewById(R.id.imageView_icon);
        layout = findViewById(R.id.linearLayoutCompat_icon_sensor_value);

        textViewNomeSensor = findViewById(R.id.textView_sensor_nome);
        textViewSensorValue = findViewById(R.id.textView_sensor_value);
        textViewSensorCommand = findViewById(R.id.textView_sensor_command);
        textViewSensorDate = findViewById(R.id.textView_sensor_date);
        fab = findViewById(R.id.fab_sensor);

        toolbarSensorValues = (Toolbar) findViewById(R.id.toolbar_Sensor_value);
        toolbarSensorValues.setTitle("Sensor "+ sensor.getName());
        setSupportActionBar(toolbarSensorValues);
    }

    private void initialUIListeners() {
        iconSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceConnection.getServiceBluetooth().sendCommand(sensor.getCommand());
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!sensor.getUsed_at().isEmpty()) {
                    try{
                        getSensorValue();
                        sensorDAO.updateSensorValue(sensor);
                        exitSensor();
                    } catch (Exception e) {
                        Snackbar.make(v, "Error ao salvar valor "+e, Snackbar.LENGTH_LONG)
                                .setAction("Ok", null).show();
                    }
                } else {
                    print.toast(getApplicationContext(), "Nada para salvar", true);
                }
            }
        });
    }

    private void snackMsg(View v, String msg) {
        Snackbar.make(v, msg, Snackbar.LENGTH_LONG)
                .setAction("Ok", null).show();
    }

    private void exitSensor() throws InterruptedException {
        finish();
    }

    private void setUIValuesInitilized() {
        textViewNomeSensor.setText(sensor.getName());
        textViewSensorCommand.setText(sensor.getCommand());
        setUiIconInitialized();
    }

    private void setUiIconInitialized() {
        try{
            IconsApp iconAppT = getIconApp(sensor.getIcon());
            String uri = "@drawable/" + iconAppT.getIcon();
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            imageViewIcon.setImageDrawable(res);
        } catch (Exception e) {

        }
    }

    IconsApp getIconApp(String id) {
        IconsAppDAO iconsAppDAOT = new IconsAppDAO(getApplicationContext());
        List<IconsApp> iconsAppListT = iconsAppDAOT.all();
        IconsApp iconsAppT = iconsAppListT.get(Integer.parseInt(id));
        return  iconsAppT;
    }

    private void setUIValue(final String value) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textViewSensorValue.setText(value);
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void setUIDate() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textViewSensorDate.setText(sensor.getUsed_at()+"h");
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void initialSharedPreferences() {

        sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                String msg = sharedPreferences.getString("mensagem_chat", " -Tente novamente");
                String[] mensagemChatCode = msg.split("-");

                if(!mensagemChatCode[0].contains("0")) {
                    sensor.setUsed_at(tools.getFullDateHour());
                    setUIValue(mensagemChatCode[1]);
                    setUIDate();
                }

            }
        };
        sharedPreference.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        // sharedPreferenceTerminal = getSharedPreferences(getString(R.string.pref_msg_terminal), Context.MODE_PRIVATE);
        sharedPreferenceEditor = sharedPreference.edit();
    }

    private void bindService() {
        intentService = new Intent(this, ServiceBluetooth.class);
        serviceConnection = new ServiceConnectionBluetoothBind();
        getApplicationContext().bindService(intentService, serviceConnection, 0);
    }
}
