package br.com.example.andreatto.tccmakerbluetooth.views.terminal.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceConnectionBluetoothBind;

public class TerminalChat extends AppCompatActivity {

    private Toolbar toolbarTerminal;
    private FloatingActionButton sendMsg;
    private EditText ediTextMsg;
    private ListView listView;

    // SERVICE Bluetooth
    private Intent intentService;
    private ServiceConnectionBluetoothBind serviceConnection;

    // MENSAGENS
    private TerminalPojo terminalPojo = new TerminalPojo();
    private TerminalMsg terminalMsg;
    private List<TerminalMsg> terminalMsgs = new ArrayList<>();
    private TerminalAdapter terminalAdapter =  new TerminalAdapter(this, terminalMsgs);

    // Preferences
    private SharedPreferences sharedPreferenceTerminal;
    private SharedPreferences.Editor sharedPreferenceEditor;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal_chat);

        initialSharedPreferences();

        initializeComponentsViews();
        initialListView();
        initializeListeners();
        bindService();

    }

    private void initialListView() {
        /**
         * Define o Adapter que irá mostrar os dados na ListView. */
        listView = findViewById(R.id.terminal_chat_historico);
        listView.setAdapter(terminalAdapter);
        //setListAdapter(terminalAdapter);
    }

    private void initialSharedPreferences() {

        sharedPreferenceTerminal = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                String msg = sharedPreferences.getString("mensagem_chat", "Tente novamente");
                String[] mensagemChatCode = msg.split("-");

                terminalMsg = terminalPojo.getTerminalMsg(); // objeto cria mensagem
                terminalMsg.setMensagem(mensagemChatCode[1]); // mensagem escreve texto
                terminalMsg.setCode(Integer.parseInt(mensagemChatCode[0])); // mensagem escreve codigo cliente
                terminalAdapter.addMensagem(terminalMsg); // add lista de mensagem

            }
        };
        sharedPreferenceTerminal.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        // sharedPreferenceTerminal = getSharedPreferences(getString(R.string.pref_msg_terminal), Context.MODE_PRIVATE);
        sharedPreferenceEditor = sharedPreferenceTerminal.edit();

    }

    private void initializeComponentsViews() {

        toolbarTerminal = findViewById(R.id.toolbar_terminal);
        toolbarTerminal.setTitle("Terminal Chat");
        setSupportActionBar(toolbarTerminal);

        ediTextMsg = (EditText) findViewById(R.id.editText_terminal_msg);
        sendMsg = (FloatingActionButton) findViewById(R.id.btn_send_terminal_chat);

    }

    private void initializeListeners() {

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ediTextMsg.getText().toString().isEmpty()) {
                    serviceConnection.getServiceBluetooth().enviarComando(ediTextMsg.getText().toString());
                }
            }
        });

    }

    private void bindService() {
        intentService = new Intent(this, ServiceBluetooth.class);
        serviceConnection = new ServiceConnectionBluetoothBind();
        getApplicationContext().bindService(intentService, serviceConnection, 0);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
