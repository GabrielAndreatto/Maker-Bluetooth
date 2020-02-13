package br.com.example.andreatto.tccmakerbluetooth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.com.example.andreatto.tccmakerbluetooth.util.AgileTools;
import br.com.example.andreatto.tccmakerbluetooth.views.form.BoardFormActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.form.FormSensor;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.buttons.ButtonsList;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.sensors.SensorListActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.terminal.chat.TerminalChat;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.board.BoardListActivity;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceConnectionBluetoothBind;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    // card que envia comando
    private AppCompatButton btnSendCmd;
    private AppCompatEditText commandSending;

    // SERVICE
    // 1 - Bluetooth
    private ServiceBluetooth serviceBluetooth;
    private Intent intentService;
    private ServiceConnectionBluetoothBind serviceConnection;

    private AgileTools tools = new AgileTools();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponentsViews();
        initializeComponentsEvents();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initializeComponentsViews();
        initializeComponentsEvents();
    }

    private void initializeComponentsViews() {
        toolbar = findViewById(R.id.toolbar_board);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        // Card enviar 1 comando
        btnSendCmd = (AppCompatButton) findViewById(R.id.button_send_cmd);
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
                startActivity(new Intent(getApplicationContext(), SensorListActivity.class));
                //Toast.makeText(getApplicationContext(), "Data e Hora: "+ tools.getFullDateHour(), Toast.LENGTH_LONG).show();
            }
        });

    }

    // Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    // Toolbar item menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_menu:
                Toast.makeText(this, "Adicionar", Toast.LENGTH_SHORT).show();
                break;

            case R.id.about_menu:
                Toast.makeText(this, "Sobre ", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // NAVIGATION DRAWER
    // navigation drawer Menu toggle
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.menu_navigation_board: {
                startActivity(new Intent(this, BoardListActivity.class));
                break;
            }
            case R.id.menu_navigation_btns: {
                startActivity(new Intent(this, ButtonsList.class));
                break;
            }
            case R.id.menu_navigation_cards: {
                startActivity(new Intent(this, SensorListActivity.class));
                break;
            }
            case R.id.menu_navigation_terminal: {
                startActivity(new Intent(this, TerminalChat.class));
                break;
            }
            case R.id.menu_navigation_board_add: {
                Bundle pkg = new Bundle();
                pkg.putString("code", "adicionarToolbar");
                Intent i = new Intent(new Intent(this, BoardFormActivity.class));
                i.putExtras(pkg);
                startActivity(i);
                break;
            }
            case R.id.menu_navigation_card_add: {
                Bundle pkg = new Bundle();
                pkg.putString("code", "new");
                Intent i = new Intent(new Intent(this, FormSensor.class));
                i.putExtras(pkg);
                startActivity(i);
                break;
            }
            default:
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
