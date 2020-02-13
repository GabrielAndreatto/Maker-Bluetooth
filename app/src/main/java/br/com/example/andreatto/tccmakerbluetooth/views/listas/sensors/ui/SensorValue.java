package br.com.example.andreatto.tccmakerbluetooth.views.listas.sensors.ui;

import android.content.Intent;
import android.content.SharedPreferences;
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
import br.com.example.andreatto.tccmakerbluetooth.dao.SensorDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Sensor;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceConnectionBluetoothBind;
import br.com.example.andreatto.tccmakerbluetooth.util.AgileTools;

public class SensorValue extends AppCompatActivity {

    Toolbar toolbarSensorValues;

    private ImageView iconSensor;
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

    private AgileTools tools = new AgileTools();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_value);

        toolbarSensorValues = (Toolbar) findViewById(R.id.toolbar_board);
        toolbarSensorValues.setTitle("Sensor Value");
        setSupportActionBar(toolbarSensorValues);

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
        initialSharedPreferences();
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
        layout = findViewById(R.id.linearLayoutCompat_icon_sensor_value);

        textViewNomeSensor = findViewById(R.id.textView_sensor_nome);
        textViewSensorValue = findViewById(R.id.textView_sensor_value);
        textViewSensorCommand = findViewById(R.id.textView_sensor_command);
        textViewSensorDate = findViewById(R.id.textView_sensor_date);
        fab = findViewById(R.id.fab_sensor);
    }

    private void initialUIListeners() {
        iconSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceConnection.getServiceBluetooth().enviarComando(sensor.getCommand());
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    getSensorValue();
                    sensorDAO.updateSensorValue(sensor);
                    exitSensor();
                } catch (Exception e) {
                    Snackbar.make(v, "Error ao salvar valor "+e, Snackbar.LENGTH_LONG)
                            .setAction("Ok", null).show();
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

    private void setUIDate(final String date) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textViewSensorDate.setText(date+"h");
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void bindService() {
        intentService = new Intent(this, ServiceBluetooth.class);
        serviceConnection = new ServiceConnectionBluetoothBind();
        getApplicationContext().bindService(intentService, serviceConnection, 0);
    }

    private void initialSharedPreferences() {
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                //Log.e("SharedPreferenceChanged",  key);
                String msg = sharedPreferences.getString("mensagem_chat", "Tente novamente");
                String[] mensagemChatCode = msg.split("-");

                if(mensagemChatCode[0].contains("1")) {
                    sensor.setValue(mensagemChatCode[1]);
                    setUIValue(mensagemChatCode[1]);
                    sensor.setUsed_at(tools.getFullDateHour());
                    setUIDate(tools.getFullDateHour());
                }
            }
        };
        sharedPreference.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        // sharedPreferenceTerminal = getSharedPreferences(getString(R.string.pref_msg_terminal), Context.MODE_PRIVATE);
        sharedPreferenceEditor = sharedPreference.edit();
    }
}
