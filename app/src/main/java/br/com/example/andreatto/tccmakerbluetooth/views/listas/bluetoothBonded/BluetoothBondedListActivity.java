package br.com.example.andreatto.tccmakerbluetooth.views.listas.bluetoothBonded;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.activitys.AppCompatActivityBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.classes.Print;

public class BluetoothBondedListActivity extends AppCompatActivityBluetooth {

    private static final String LOG_PAGE = "BluetoothBondedListActivity";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerBluetoothBondedListAdapter adapter;
    private List<BluetoothDevice> mBluetoothDeviceList;
    private ProgressDialog dialog;

    private Print print = new Print();
    private Bundle pacote = new Bundle();
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_bonded_list);

        pacote = getIntent().getExtras();
        if(!pacote.isEmpty() || getIntent().getExtras() != null) {

            code = pacote.getString("code");
            print.logE(LOG_PAGE, code);

            if (code.equals("bluetooth-none")) {
                print.logE(LOG_PAGE, "bluetooth-none");
            }

            if (code.equals("bluetooth-macAddress")) {
                print.logE(LOG_PAGE, "bluetooth-macAddress");
            }

            if (code.equals("bluetooth-edit")) {
                if(!pacote.getString("id_board").isEmpty()) {
                    pacote.getString("id_board");
                    print.logE(LOG_PAGE, "bluetooth-edit-id: "+pacote.getString("id_board"));
                }
            }
        }

        mBluetoothDeviceList = new ArrayList<BluetoothDevice>(bluetoothAdapter.getBondedDevices());
        // Registra o receiver para receber as mensagens de dispositivos pareados
        this.registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        // Registra o receiver para receber a mensagem do final da busca.
        this.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        // Garante que não existe outra busca sendo realizada
        if(bluetoothAdapter.isDiscovering()) {
            // Dispara a busca
            bluetoothAdapter.cancelDiscovery();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initial();
    }

    public void initial() {
        toolbar = findViewById(R.id.toolbar_bluetooth_bonded_list);
        toolbar.setTitle("Bluetooth pareado");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView_bluetooth_bonded);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initialAdpter();
    }

    public void initialAdpter() {
        if (code.equals("bluetooth-edit")) {
            adapter = new RecyclerBluetoothBondedListAdapter(this, code, pacote.getString("id_board"), mBluetoothDeviceList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter = new RecyclerBluetoothBondedListAdapter(this, code, pacote.getString("id_board"), mBluetoothDeviceList);
            recyclerView.setAdapter(adapter);
        }
    }

    // Receiver para receber os broadcasts do Bluetooth
    private final BroadcastReceiver mReceiver  = new BroadcastReceiver() {

        // Quantidade de dispositivos encontrados
        private int count;

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            // Se um device foi encontrado
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                Log.e("mReceiver", "Bluetooth Iniciou o ACTION_FOUND");

                // Recupera o device da intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // Apenas insere na lista os devices que ainda não estão pareados
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mBluetoothDeviceList.add(device);
                    Toast.makeText(context, "Encontrou: " + device.getName()+":" +
                            device.getAddress(), Toast.LENGTH_SHORT).show();
                    count++;
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                // Iniciou a busca
                count = 0;
                Toast.makeText(context, "Busca iniciada.", Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // Terminou a busca
                Toast.makeText(context, "Busca finalizada. " + count + " novos devices pareados encontrados", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                initialAdpter();
            }

        }
    };

    public void discoveryBluetooth() {
        // Garante que não existe outra busca sendo realizada
        if(bluetoothAdapter.isDiscovering()) {
            // Dispara a busca
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
        dialog = ProgressDialog.show(this, "Bluetooth pareado", "Buscando Bluetooth...", false, true);
    }

    // Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_bluetooth_bonded, menu);
        return true;
    }

    // Toolbar item menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon_refresh:
                // buscar novos bluetooth pareados
                discoveryBluetooth();
                break;
            case R.id.about_menu:
                Toast.makeText(this, "Sobre ", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Log.e("onRestart", "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Garante que a busca é cancelada ao sair
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
        // Cancela o registro do receiver
        this.unregisterReceiver(mReceiver);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case 101:
                Log.e("onActivityResult", "requestCode: " + requestCode);
                Bundle pkg = intent.getExtras();

                if(pkg.getString("code").equals("bluetooth-returned")) {

                    Log.e("onActivityResult", "onActivityResult: " + pkg.getString("mac-address"));

                    print.toast(getApplicationContext(), "onActivityResult: " + pkg.getString("mac-address"), true);
                    initial();
                }

                break;

        }
    }
}
