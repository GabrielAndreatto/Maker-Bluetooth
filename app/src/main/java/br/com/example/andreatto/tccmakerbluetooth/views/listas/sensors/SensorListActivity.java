package br.com.example.andreatto.tccmakerbluetooth.views.listas.sensors;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.SensorDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Sensor;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceConnectionBluetoothBind;
import br.com.example.andreatto.tccmakerbluetooth.util.Print;
import br.com.example.andreatto.tccmakerbluetooth.views.form.FormSensor;

public class SensorListActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private RecyclerSensorListAdapter adapter;
    private List<Sensor> sensors;

    private ServiceBluetooth serviceBluetooth;
    private Intent intentService;
    private ServiceConnectionBluetoothBind serviceConnection;

    private Print print = new Print();
    private Bundle pkg;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);
        initial();
    }

    private void initial() {
        try {
            SensorDAO sensorDAO = new SensorDAO(getApplicationContext());
            sensors = sensorDAO.all();
        } catch (Exception e) {
            print("SensorDAO query.all() ERROR", true);
        }

        recyclerView = findViewById(R.id.recyclerView_sensor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerSensorListAdapter(sensors, this);
        recyclerView.setAdapter(adapter);

        intentService = new Intent(this, ServiceBluetooth.class);
        serviceConnection = new ServiceConnectionBluetoothBind();
        bindService(intentService, serviceConnection, 0);

        toolbar = findViewById(R.id.toolbar_sensor);
        // toolbar.setTitle("Lista de Board  (" + boards.size() + ") ");
        toolbar.setTitle("Sensores");
        setSupportActionBar(toolbar);
    }

    // Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_sensor, menu);
        return true;
    }

    // Toolbar item menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_menu:
                Bundle pkg = new Bundle();
                pkg.putString("code", "new");
                Intent i = new Intent(this, FormSensor.class);
                i.putExtras(pkg);
                startActivityForResult(i, 200);
                break;
            case R.id.about_menu:
                Toast.makeText(this, "Sobre ", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode){
            case 200:
                if(resultCode != 0) {
                    Log.e("requestCode", String.valueOf(requestCode));
                    Log.e("resultCode", String.valueOf(resultCode));
                    if(!intent.getExtras().isEmpty()) {
                        pkg = intent.getExtras();
                        if(intent.getExtras() != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initial();
                                    if(pkg.getString("action").contains("new")) {
                                        print("Cadastro realizado com sucesso", true);
                                    }
                                    if(pkg.getString("action").contains("edit")){
                                        print("Editado com sucesso", true);
                                    }
                                }
                            });

                        }
                    }
                }

                break;
            case 201:
                initial();
                break;
        }
    }

    void print(String msg, Boolean longTime) {
        print.toast(getApplicationContext(), msg, longTime);
    }

    void logE(String tag, String msg) {
        print.logE(tag, msg);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logE("onRestart", "onRestart");
        initial();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}