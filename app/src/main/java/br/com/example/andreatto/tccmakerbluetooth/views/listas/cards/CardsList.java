package br.com.example.andreatto.tccmakerbluetooth.views.listas.cards;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.BoardDAO;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceConnectionBluetoothBind;
import br.com.example.andreatto.tccmakerbluetooth.views.form.BoardFormActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.board.BoardListActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.board.RecyclerBoardListAdapter;

public class CardsList extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton btnSensor; // btn_sensor
    private TextView sensorValue;

    // SERVICE
    // 1 - Bluetooth
    private ServiceBluetooth serviceBluetooth;
    private Intent intentService;
    private ServiceConnectionBluetoothBind serviceConnection;

    // Preferences
    private SharedPreferences sharedPreferenceSensor;
    private SharedPreferences.Editor sharedPreferenceEditor;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_list);

        initialViews();
        initializeComponentsEvents();
        initialSharedPreferences();
    }

    public void initialViews() {

        toolbar = findViewById(R.id.toolbar_card);
        // toolbar.setTitle("Lista de Board  (" + boards.size() + ") ");
        toolbar.setTitle("Lista de Cards");
        setSupportActionBar(toolbar);

        btnSensor = (ImageButton) findViewById(R.id.btn_sensor_main);
        sensorValue = (TextView) findViewById(R.id.sensor_valor);

        // SERVICE
        // 1 - Bluetooth
        intentService = new Intent(this, ServiceBluetooth.class);
        serviceConnection = new ServiceConnectionBluetoothBind();
        bindService(intentService, serviceConnection, 0);

    }

    private void initializeComponentsEvents() {

        btnSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String valorSensor;
                try {
                    valorSensor = serviceConnection.getServiceBluetooth().getValueSensor("h");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sensorValue.setText(valorSensor);
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void initialSharedPreferences() {

        sharedPreferenceSensor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                String msg = sharedPreferences.getString("sensor", "Tente novamente");

                //sensorValue.setText(msg);

            }
        };
        sharedPreferenceSensor.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        // sharedPreferenceTerminal = getSharedPreferences(getString(R.string.pref_msg_terminal), Context.MODE_PRIVATE);
        sharedPreferenceEditor = sharedPreferenceSensor.edit();

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
