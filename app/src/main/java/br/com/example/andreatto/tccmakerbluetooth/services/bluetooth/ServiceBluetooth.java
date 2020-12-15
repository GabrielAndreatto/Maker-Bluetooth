package br.com.example.andreatto.tccmakerbluetooth.services.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.PublicKey;
import java.util.UUID;

public class ServiceBluetooth extends Service {

    private IBinder binder = new BinderBluetooth(this);

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothDevice bluetoohRemoto;
    protected BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket = null;
    private BluetoothServerSocket serverSocket;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;
    private DataInputStream is;
    private DataOutputStream os;

    // Preferences
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor sharedPreferenceEditor;

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothAdapter = bluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    public void conectarBluetooth(String mac_address){
        this.bluetoothSocket = conectBluetooth(mac_address);
    }

    public BluetoothSocket conectBluetooth(String mac_address) {
        bluetoohRemoto = bluetoothAdapter.getRemoteDevice(mac_address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bluetoothSocket = bluetoohRemoto.createRfcommSocketToServiceRecord(MY_UUID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bluetoothAdapter.cancelDiscovery();
                try {
                    bluetoothSocket.connect();
                } catch (IOException e) {
                    Log.d("BLTTH_SERVICE", "... Close socket during connection failure ...");
                } finally {
                    sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sharedPreferenceEditor = sharedPreference.edit();

                    initialBluetoothIO();
                }
            }

        }).start();
        return bluetoothSocket;
    }

    public void initialBluetoothIO() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (bluetoothSocket.isConnected()) {
                    try {
                        //outputStream = bluetoothSocket.getOutputStream();
                        inputStream = bluetoothSocket.getInputStream();

                        is = new DataInputStream(inputStream);
                        os = new DataOutputStream(bluetoothSocket.getOutputStream());

                        initialServerComunication(true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void initialServerComunication(final boolean state) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (bluetoothSocket.isConnected()) {
                    try {
                        while (state == true) {
                            if (inputStream.read() > 0) {

                                final byte[] msgBuffer = new byte[1024];
                                inputStream.read(msgBuffer);

                                sharedPreferenceEditor.putString("mensagem_chat", 1 + "-" + new String(msgBuffer));
                                sharedPreferenceEditor.apply();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void sendCommand(final String cmd) {
        sharedPreferenceEditor.putString("mensagem_chat", 0 + "-" + cmd);
        sharedPreferenceEditor.apply();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(bluetoothSocket.isConnected()){
                    try{
                        //outputStream.write(msgBufferTwo);
                        os.writeUTF(cmd);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                                "Bluetooth não está conectado",
                                Toast.LENGTH_LONG)
                            .show();
                }

            }
        }).start();
    }


}