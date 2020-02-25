package br.com.example.andreatto.tccmakerbluetooth.views.listas.bluetoothActived;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.activitys.AppCompatActivityBluetooth;

public class BluetoothActivedListActivity extends AppCompatActivityBluetooth implements AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private ProgressDialog dialog;
    protected List<BluetoothDevice> lista;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_actived_list);

        // Inicia a lista com os devices pareados
        lista = new ArrayList<BluetoothDevice>();
        // Registra o receiver para receber as mensagens de dispositivos pareados
        this.registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        // Registra o receiver para receber a mensagem do final da busca.
        this.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        // Garante que não existe outra busca sendo realizada
        if(bluetoothAdapter.isDiscovering()) {
            // Dispara a busca
            bluetoothAdapter.cancelDiscovery();
        }

        initial();

    }

    private void initial() {

        listview = (ListView) findViewById(R.id.listView_bluetooth_actived);

        toolbar = findViewById(R.id.toolbar_bluetooth_actived_list);
        toolbar.setTitle("Bluetooth próximo");
        setSupportActionBar(toolbar);
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
                // Recupera o device da intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // Apenas insere na lista os devices que ainda não estão pareados
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    lista.add(device);
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
                Toast.makeText(context, "Busca finalizada. " + count + " devices encontrados", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                // Atualiza o listview. Agora vai ter todos os devices pareados,
                // mais os novos que foram encontrados
                updateLista();
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
        dialog = ProgressDialog.show(this, "Bluetooth próximo", "Buscando Bluetooth...", false, true);

    }

    private void updateLista() {
        // Cria o array com o nome de cada device
        List nomes = new ArrayList();
        for (BluetoothDevice device : lista) {
            nomes.add(device.getName() + " - " + device.getAddress());
        }

        // Cria o adapter para popular o Listview
        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter adapter = new ArrayAdapter(this, layout, nomes);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
        toolbar.setTitle("Bluetooth próximo");
        if(lista.size() <= 0) {
            TextView text = new TextView(this);
            text.setText("Nenhum bluetooth próximo para pariar");
            text.setEnabled(true);
            text.setGravity(1);
        }
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Recupera o device selecionado
        BluetoothDevice device = lista.get((int)id);
        String msg = device.getName() + " - " + device.getAddress();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    // Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_bluetooth_actived, menu);
        return true;
    }

    // Toolbar item menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon_refresh_actived:
                // buscar novos bluetooth pareados
                discoveryBluetooth();
                break;
            case R.id.about_menu:
                Toast.makeText(this, R.string.sobre, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
