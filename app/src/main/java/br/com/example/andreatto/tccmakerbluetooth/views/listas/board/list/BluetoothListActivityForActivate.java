package br.com.example.andreatto.tccmakerbluetooth.views.listas.board.list;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.BoardDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Board;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.activitys.AppCompatActivityBluetooth;

public class BluetoothListActivityForActivate extends AppCompatActivityBluetooth implements AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private ProgressDialog dialog;
    private ListView listview;
    private List<BluetoothDevice> mBluetoothDeviceList;
    private String id_board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list_for_activate);

        Bundle bundle =  getIntent().getExtras().getBundle("idBoard");
        id_board = bundle.getString("id_board");

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

        // Recupera o board selecionado
        Board board = new Board();
        BoardDAO boardDAO = new BoardDAO(this);
        boardDAO.atualizarBluetoothBoard(id_board, device.getAddress(), device.getName());
        //final String msg = device.getName() + " - " + device.getAddress();

        Intent returnIntent = new Intent();
        setResult(this.RESULT_OK,returnIntent);
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
