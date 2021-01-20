package br.com.example.andreatto.tccmakerbluetooth.views.listas.sensors.api;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.dao.SensorDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Sensor;
import br.com.example.andreatto.tccmakerbluetooth.utils.classes.Print;

public class DeleteSensorActivity extends AppCompatActivity {

    private Print print = new Print();
    private Bundle pacote = new Bundle();
    private Bundle pkg = new Bundle();
    private String code;

    private int sensorId;
    private Sensor sensor;

    private SensorDAO sensorDAO = new SensorDAO(this);
    private List<Sensor> sensorList = new ArrayList<Sensor>();
    private String nameSensorDelete;
    private int idSensorDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pacote = getIntent().getExtras();
        if(!pacote.isEmpty() || getIntent().getExtras() != null) {

            code = pacote.getString("code");

            print.logE("CODE", code);

            if (code.contains("delete")) {
                sensorId = pacote.getInt("sensor_id");
                sensor = getSensor();
                nameSensorDelete = sensor.getName();
                idSensorDelete = (int) sensor.getId();
            }
        }

        print.logE("SENSOR", "Id: "+sensor.getId());
        print.logE("SENSOR", "Name: "+sensor.getName());
        print.logE("SENSOR", "Command: "+sensor.getCommand());
        print.logE("SENSOR", "Value: "+sensor.getValue());
        print.logE("SENSOR", "Icon: "+sensor.getIcon());
        print.logE("SENSOR", "Used At: "+sensor.getUsed_at());
        print.logE("SENSOR", "Created At: "+sensor.getCreated_at());
        print.logE("SENSOR", "Updated At: "+sensor.getUpdated_at());

        deleteSensor();

    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void deleteSensor() {
        try{
            sensorDAO.remover(sensor);
            // actuatorDAO.removerWithId(actuatorId);
        } catch (Exception e) {
            e.getMessage();
        }
    }


}
