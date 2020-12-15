package br.com.example.andreatto.tccmakerbluetooth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceConnectionBluetoothBind;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.classes.Print;
import br.com.example.andreatto.tccmakerbluetooth.views.form.actuator.ActuatorFormActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.form.board.BoardFormActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.form.sensor.SensorFormActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.actuators.ActuatorActivityTab;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.bluetoothActived.BluetoothActivedListActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.bluetoothBonded.BluetoothBondedListActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.boards.BoardListActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.sensors.SensorListActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.terminal.chat.TerminalChat;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Print print = new Print();

    // card que envia comando
    private AppCompatButton btnSendCmd;
    private AppCompatEditText commandSending;

    // SERVICE
    // 1 - Bluetooth
    private ServiceBluetooth serviceBluetooth;
    private Intent intentService;
    private ServiceConnectionBluetoothBind serviceConnection;

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
//        btnSendCmd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                serviceConnection.getServiceBluetooth().sendCommand(commandSending.getText().toString());
//            }
//        });
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
            case R.id.about_menu:
                print.toast(getApplicationContext(), "Sobre", true);
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
            case R.id.menu_navigation_actuators: {
                startActivity(new Intent(this, ActuatorActivityTab.class));
                break;
            }
            case R.id.menu_navigation_sensors: {
                startActivity(new Intent(this, SensorListActivity.class));
                break;
            }
            case R.id.menu_navigation_terminal: {
                startActivity(new Intent(this, TerminalChat.class));
                break;
            }
            case R.id.menu_navigation_bluetooth_pareado: {
                Bundle pkg = new Bundle();
                pkg.putString("code", "bluetooth-none");
                Intent i = new Intent(new Intent(this, BluetoothBondedListActivity.class));
                i.putExtras(pkg);
                startActivityForResult(i, 101);
                break;
            }
            case R.id.menu_navigation_bluetooth_proximos: {
                startActivity(new Intent(this, BluetoothActivedListActivity.class));
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
            case R.id.menu_navigation_actuator_add: {
                Bundle pkg = new Bundle();
                pkg.putString("code", "new");
                Intent i = new Intent(new Intent(this, ActuatorFormActivity.class));
                i.putExtras(pkg);
                startActivity(i);
                break;
            }
            case R.id.menu_navigation_sensor_add: {
                Bundle pkg = new Bundle();
                pkg.putString("code", "new");
                Intent i = new Intent(new Intent(this, SensorFormActivity.class));
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
