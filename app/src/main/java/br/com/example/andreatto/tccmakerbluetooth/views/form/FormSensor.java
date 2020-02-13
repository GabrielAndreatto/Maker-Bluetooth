package br.com.example.andreatto.tccmakerbluetooth.views.form;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.SensorDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Sensor;
import br.com.example.andreatto.tccmakerbluetooth.util.Print;

public class FormSensor extends AppCompatActivity {

    private Toolbar toolbarSensor;
    private Print print = new Print();


    private EditText editTextName, editTextCommand;
    private Button save, cancel;

    private int sensorId;
    private Sensor sensor;
    private SensorDAO sensorDAO = new SensorDAO(this);
    private List<Sensor> sensorList = new ArrayList<Sensor>();
    private Bundle pacote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_sensor);

        initialUI();
        pacote = getIntent().getExtras();
        if(!pacote.isEmpty() || getIntent().getExtras() != null) {
            if (pacote.getString("code").contains("new")) {
                sensor = new Sensor();
            }
            if (pacote.getString("code").contains("edit")) {
                sensorId = pacote.getInt("sensor_id");
                sensor = getSensor();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialUIlistener();
        setUIValuesInitilized();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        clearInputsUI();
        initialUI();
        initialUIlistener();
    }

    private void initialUIlistener() {
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
                            if(pacote.getString("code").contains("edit"))
                                print.toast(getApplicationContext(), "Sensor editado com sucesso", true);
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
                onRestart();
            }
        });
    }

    public void returnActivityOn() {
        Bundle pkg = new Bundle();
        pkg.putString("action", pacote.getString("code"));
        Intent returnIntent = new Intent();
        returnIntent.putExtras(pkg);
        setResult(200, returnIntent);
        finish();
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

    private void setUIValuesInitilized() {
        editTextName.setText(this.sensor.getName());
        editTextCommand.setText(this.sensor.getCommand());
    }

    private void initialUI() {
        editTextName = findViewById(R.id.sensor_name);
        editTextCommand = findViewById(R.id.sensor_cmd);
        save = findViewById(R.id.btn_save);
        cancel = findViewById(R.id.btn_cancel);

        toolbarSensor = findViewById(R.id.toolbar_board);
        // toolbar.setTitle("Lista de Board  (" + boards.size() + ") ");
        toolbarSensor.setTitle("Sensores");
        setSupportActionBar(toolbarSensor);
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
