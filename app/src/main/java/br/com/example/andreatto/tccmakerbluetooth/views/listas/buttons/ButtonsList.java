package br.com.example.andreatto.tccmakerbluetooth.views.listas.buttons;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceConnectionBluetoothBind;

public class ButtonsList extends AppCompatActivity {

    private Toolbar toolbar;

    // card que envia comando
    private ImageButton btnSendCmd;
    private ImageView btnedit;
    private AppCompatEditText commandSending;

    // SERVICE
    // 1 - Bluetooth
    private ServiceBluetooth serviceBluetooth;
    private Intent intentService;
    private ServiceConnectionBluetoothBind serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons_list);

        initialViews();
        initializeComponentsEvents();
        //initialSharedPreferences();
    }

    private void initialViews() {
        toolbar = findViewById(R.id.toolbar_btn);
        // toolbar.setTitle("Lista de Board  (" + boards.size() + ") ");
        toolbar.setTitle("Lista de botões");
        setSupportActionBar(toolbar);

        // Card enviar 1 comando
        btnSendCmd = (ImageButton) findViewById(R.id.btn_send);
        btnedit = (ImageView) findViewById(R.id.imageView_editar);
        commandSending = (AppCompatEditText) findViewById(R.id.edit_text_cmd);

        // SERVICE
        // 1 - Bluetooth
        intentService = new Intent(this, ServiceBluetooth.class);
        serviceConnection = new ServiceConnectionBluetoothBind();
        bindService(intentService, serviceConnection, 0);

    }

    private void initializeComponentsEvents() {

        btnSendCmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SERVICE
                // 1 - Bluetooth
                serviceConnection.getServiceBluetooth().enviarComando("A");
            }
        });

        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("BOTAO_EDIT", "Botão edit do card botões");
            }
        });
    }

    // Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_board, menu);
        return true;
    }
    // Toolbar item menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_menu:

                Toast.makeText(this, "Adicionar", Toast.LENGTH_SHORT).show();
//                Bundle pkg = new Bundle();
//                pkg.putString("code", "adicionarToolbar");
//                Intent i = new Intent(BoardListActivity.this, BoardFormActivity.class);
//                i.putExtras(pkg);
//                startActivity(i);
                break;

            case R.id.about_menu:
                Toast.makeText(this, "Sobre ", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
