package br.com.example.andreatto.tccmakerbluetooth.views.form.actuator;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.ActuactorDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Actuator;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.classes.Print;

public class ActuatorFormActivity extends AppCompatActivity {

    Print print = new Print();
    Toolbar toolbar;
    private FloatingActionButton fab;

    Bundle pacote = new Bundle();
    String code;

    RadioButton radioToggle, radioOnoff;
    Switch switchActuator;

    String name, commandOn, commandOff;
    TextInputEditText textInputEditTextName;
    TextInputEditText textInputEditTextCmdOn;
    TextInputEditText textInputEditTextCmdOff;

    Actuator actuator;
    List<Actuator> actuatorList = new ArrayList<>();
    ActuactorDAO actuatorDAO = new ActuactorDAO(this);
    private int actuatorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actuator);

        pacote = getIntent().getExtras();
        if(!pacote.isEmpty() || getIntent().getExtras() != null) {

            code = pacote.getString("code");
            print.logE("CODE", code);

            if (code.contains("new")) {
                actuator = new Actuator();
            }

            initialUI();
            initialUIListeners();

            if (code.contains("edit")) {
                actuatorId = pacote.getInt("actuator_id");
                actuator = getActuator();
                initialValuesUI();
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

    }

    private void initialUI() {
        toolbar = findViewById(R.id.toolbar_actuator);
        toolbar.setTitle("Atuadores");
        setSupportActionBar(toolbar);

        textInputEditTextName = findViewById(R.id.textInputEditText_name);
        textInputEditTextCmdOn = findViewById(R.id.textInputEditText_cmdOn);
        textInputEditTextCmdOff = findViewById(R.id.textInputEditText_cmdOff);

        switchActuator = findViewById(R.id.switch_actuator);
        switchActuator.setChecked(true);

        radioToggle = findViewById(R.id.radio_toggle);
        radioOnoff = findViewById(R.id.radio_onoff);

        if(code.contains("edit")) {

        } else {
            radioOnoff.setChecked(true);
        }

        fab = findViewById(R.id.fab);
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

    private void initialValuesUI() {
        if(actuator.getTipo().contains("toggle")) {
            radioToggle.setChecked(true);
            radioToggle.setClickable(true);
        } else {
            radioOnoff.setClickable(true);
            radioOnoff.setChecked(true);
        }
        textInputEditTextName.setText(actuator.getName());
        textInputEditTextCmdOn.setText(actuator.getCommandOn());
        textInputEditTextCmdOff.setText(actuator.getCommandOff());
    }

    private void initialUIListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    initialObject();
                    actuatorDAO.save(actuator);
                    if(code.contains("new")) {
                        print.toast(getApplicationContext(), "Atuador criado com sucesso!", true);
                    } else {
                        print.toast(getApplicationContext(), "Atuador editado com sucesso!", true);
                    }
                } catch (Exception e) {
                    e.getMessage();
                } finally {
                    finish();
                }
            }
        });
    }

    private void initialObject() {
        actuator.setName(textInputEditTextName.getText().toString());
        actuator.setCommandOn(textInputEditTextCmdOn.getText().toString());
        actuator.setCommandOff(textInputEditTextCmdOff.getText().toString());
        actuator.setTipo(radioToggle.isChecked() ? "toggle" : "onoff");
    }

    void clearUI() {
        textInputEditTextName.setText("");
        textInputEditTextCmdOn.setText("");
        textInputEditTextCmdOff.setText("");
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_toggle:
                if (checked)
                    actuator.setTipo("toggle");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            radioToggle.setChecked(true);
                            radioToggle.setClickable(true);
                        }
                    });
                    break;
            case R.id.radio_onoff:
                if (checked)
                    actuator.setTipo("onoff");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            radioOnoff.setClickable(true);
                            radioOnoff.setChecked(true);
                        }
                    });
                    break;
        }
    }
}
