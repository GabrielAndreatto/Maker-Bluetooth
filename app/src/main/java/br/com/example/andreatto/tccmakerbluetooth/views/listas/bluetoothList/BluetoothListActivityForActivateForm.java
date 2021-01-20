package br.com.example.andreatto.tccmakerbluetooth.views.listas.bluetoothList;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.utils.AppCompatActivityBluetooth;

public class  BluetoothListActivityForActivateForm extends AppCompatActivityBluetooth implements AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private ProgressDialog dialog;
    private ListView listview;
    private List<BluetoothDevice> mBluetoothDeviceList;
    private String id_board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list_for_activate);

        mBluetoothDeviceList = new ArrayList<BluetoothDevice>(bluetoothAdapter.getBondedDevices());

        initial();
        updateLista();

    }

    private void initial() {

        listview = (ListView) findViewById(R.id.listView_bluetooth_list_for_activate);

        toolbar = findViewById(R.id.toolbar_bluetooth_list_for_activate);
        toolbar.setTitle("Bluetooth para conectar  (" + mBluetoothDeviceList.size() + ")");
        setSupportActionBar(toolbar);
    }

    private void updateLista() {
        // Cria o array com o nome de cada device
        List nomes = new ArrayList();
        for (BluetoothDevice device : mBluetoothDeviceList) {
            nomes.add("Nome: " + device.getName() + "\nMac address: " + device.getAddress());
        }

        // Cria o adapter para popular o Listview
        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter adapter = new ArrayAdapter(this, layout, nomes);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
        toolbar.setTitle("Bluetooth  (" + mBluetoothDeviceList.size() + ")");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Recupera o device selecionado
        BluetoothDevice device = mBluetoothDeviceList.get((int)id);

        Bundle pkg = new Bundle();
        pkg.putString("nome", device.getName());
        pkg.putString("mac_address", device.getAddress());

        Intent returnIntent = new Intent();
        returnIntent.putExtras(pkg);
        setResult(this.RESULT_OK, returnIntent);
        finish();

    }

    // Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_bluetooth_for_activate, menu);
        return true;
    }

    // Toolbar item menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.about_menu:
                Toast.makeText(this, "Sobre ", Toast.LENGTH_SHORT).show();
                break;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
