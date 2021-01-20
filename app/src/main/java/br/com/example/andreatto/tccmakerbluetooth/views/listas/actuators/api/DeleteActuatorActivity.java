package br.com.example.andreatto.tccmakerbluetooth.views.listas.actuators.api;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.dao.ActuactorDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Actuator;
import br.com.example.andreatto.tccmakerbluetooth.utils.classes.Print;

public class DeleteActuatorActivity extends AppCompatActivity {

    private Print print = new Print();
    private Bundle pacote = new Bundle();
    private Bundle pkg = new Bundle();
    private String code;

    private Actuator actuator;
    private List<Actuator> actuatorList = new ArrayList<>();
    private ActuactorDAO actuatorDAO = new ActuactorDAO(this);
    private int actuatorId;
    private String nameActuatorDelete;
    private int idActuatorDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pacote = getIntent().getExtras();
        if(!pacote.isEmpty() || getIntent().getExtras() != null) {

            code = pacote.getString("code");

            print.logE("CODE", code);

            if (code.contains("delete")) {
                actuatorId = pacote.getInt("actuator_id");
                actuator = getActuator();
                nameActuatorDelete = actuator.getName();
                idActuatorDelete = actuator.getId();
            }
        }

        print.logE("ATUADOR", "Id: "+actuator.getId());
        print.logE("ATUADOR", "Name: "+actuator.getName());
        print.logE("ATUADOR", "Type: "+actuator.getTipo());
        print.logE("ATUADOR", "Command On: "+actuator.getCommandOn());
        print.logE("ATUADOR", "Command Off: "+actuator.getCommandOff());
        print.logE("ATUADOR", "Used At: "+actuator.getUsed_at());
        print.logE("ATUADOR", "Created At: "+actuator.getCreated_at());
        print.logE("ATUADOR", "Updated At: "+actuator.getUpdated_at());

        deleteActuator();
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }

    private Actuator getActuator() {
        actuatorList = actuatorDAO.all();
        Actuator actuatorT = new Actuator();
        for (Actuator tmpActuator: actuatorList) {
            if(tmpActuator.getId() == actuatorId) {
                actuatorT = tmpActuator;
            }
        }
        return actuatorT;
    }

    private void deleteActuator() {
        try{
            actuatorDAO.remover(actuator);
            // actuatorDAO.removerWithId(actuatorId);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
