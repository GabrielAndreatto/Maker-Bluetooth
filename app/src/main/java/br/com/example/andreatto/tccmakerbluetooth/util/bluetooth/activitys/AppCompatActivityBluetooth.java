package br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.activitys;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import br.com.example.andreatto.tccmakerbluetooth.dao.BoardDAO;

public class AppCompatActivityBluetooth extends AppCompatActivity {

    public SharedPreferences sPUserConf;

    // represents a bluetooth remote device
    private BluetoothDevice bluetoohRemoto;
    // Represents the local device bluetooth adapter
    protected BluetoothAdapter bluetoothAdapter;
    // A connected or connecting bluetooth socket
    private BluetoothSocket bluetoothSocket = null;

    private static final String endereco_MAC_do_Bluetooth_Remoto = "98:D3:31:F5:27:B6";
    public static final int CÓDIGO_PARA_ATIVAÇÃO_DO_BLUETOOTH = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bluetoothAdapter = bluetoothAdapter.getDefaultAdapter();
        sPUserConf = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        verificarCondiçãoDoBluetooth();
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public boolean isBluetoothDevice() {

        Boolean condition = false;

        if(bluetoothAdapter != null) {
            condition = true;
        }

        return condition;
    }

    public boolean isEnabled() {

        Boolean condition = false;

        if(bluetoothAdapter.isEnabled()) {
            condition = true;
        }
        return condition;
    }

    public void openBluetoothDevice() {
        Intent enablelntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enablelntent, CÓDIGO_PARA_ATIVAÇÃO_DO_BLUETOOTH);
    }

    public void verificarCondiçãoDoBluetooth() {

        // Get a handle to the default local Bluetooth adapter.
        // bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Verifica se o celular tem Bluetooth
        if(bluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "Dispositivo não possui adaptador Bluetooth", Toast.LENGTH_LONG).show();
            // Finaliza a aplicação.
            finish();
        } else {
            // Verifica se o bluetooth está desligado. Se sim, pede permissão para ligar.
            if(!bluetoothAdapter.isEnabled()){
                openBluetoothDevice();
            }}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case CÓDIGO_PARA_ATIVAÇÃO_DO_BLUETOOTH:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(getApplicationContext(), "Bluetooth foi ativado", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth não foi ativado", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void sendMyCode(String cmd) {

        conectarBluetooth();

        // Verifica se há conexão estabelecida com o Bluetooth.
        if(bluetoothSocket != null){

            try{

                outputStream = bluetoothSocket.getOutputStream();

                byte[] msgBuffer = cmd.getBytes();
                outputStream.write(msgBuffer);

            } catch (Exception e) {
                e.getMessage();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth não está conectado", Toast.LENGTH_LONG).show();
        }

    }

    protected void conectarBluetooth(final String id_board, final String mac_address) {

        bluetoohRemoto = getBluetoothAdapter().getRemoteDevice(mac_address);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("CON", "conectarBluetooth 2");
                try {
                    bluetoothSocket = bluetoohRemoto.createRfcommSocketToServiceRecord(MY_UUID);
                    Log.d("CONX", "... TRY btSocket createRfcommSocketToServiceRecord to Remote...");
                } catch (IOException e) {
                    Log.d("CONX", "... CATCH btSocket createRfcommSocketToServiceRecord to Remote...");
                    Log.d("CONX", "Fatal Error in onResume() and socket create failed: ");
                    e.getMessage();
                }

                // Discovery is resource intensive. Make sure it isn't going on when you attempt to connect and pass your message.
                bluetoothAdapter.cancelDiscovery();

                // Establish the connection. This will block until it connects.
                Log.d("CONX", "...Connecting to Remote...");
                try {
                    bluetoothSocket.connect();
                    //statusSocket = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            BoardDAO boardDAO = new BoardDAO(getApplicationContext());
                            boardDAO.conexBluetoothFromBoard(String.valueOf(id_board));
                        }
                    });


                    // Environment.getDataDirectory();
                    SharedPreferences.Editor editor = sPUserConf.edit();
                    editor.putString("bluetooth_mac_address", mac_address);
                    editor.putString("bluetooth_board_connected", id_board);
                    editor.putBoolean("bluetooth_loged", true);
                    editor.commit();

                } catch (IOException e) {
                    Log.d("CONX", "... In onResume() and unable to close socket during connection failure ...");
                }

                // Create a data stream so we can talk to server.
                // Establish the connection. This will block until it connects.
                /*
                Log.d("CONX", "...outStream to Remote...");
                try {
                    outStream = btSocket.getOutputStream();
                } catch (IOException e) {
                    errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
                }
                */

            }

        }).start();

        returnActivityOn();
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    protected void conectarBluetooth() {

        String mac_address = sPUserConf.getString("bluetooth_mac_address", "");

        bluetoohRemoto = getBluetoothAdapter().getRemoteDevice(mac_address);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bluetoothSocket = bluetoohRemoto.createRfcommSocketToServiceRecord(MY_UUID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("CONX", "...outStream to Remote...");
                try {
                    outputStream = bluetoothSocket.getOutputStream();

                    // Get the input stream associated with this socket.
                    inputStream = bluetoothSocket.getInputStream();


                } catch (IOException e) {
                    e.getMessage();
                }
                Log.d("CONX", "... TRY btSocket createRfcommSocketToServiceRecord to Remote...");
            }

        }).start();

        returnActivityOn();
    }

    public void returnActivityOn() {

        Bundle pkg = new Bundle();
        pkg.putString("response", "ok");

        Intent returnIntent = new Intent();
        returnIntent.putExtra("bluetooth_conection", pkg);
        setResult(this.RESULT_OK,returnIntent);
        finish();

    }
}
