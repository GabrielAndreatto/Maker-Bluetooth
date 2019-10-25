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
import java.util.UUID;

public class ServiceBluetooth extends Service {

    private IBinder binder = new BinderBluetooth(this);

    // BLUETOOTH
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // represents a bluetooth remote device
    private BluetoothDevice bluetoohRemoto;
    // Represents the local device bluetooth adapter
    protected BluetoothAdapter bluetoothAdapter;
    // A connected or connecting bluetooth socket
    private BluetoothSocket bluetoothSocket = null;
    private BluetoothServerSocket serverSocket;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;
    private DataInputStream is;
    private DataOutputStream os;

    // Preferences
    private SharedPreferences sharedPreferenceTerminal;
    private SharedPreferences.Editor sharedPreferenceEditor;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreate() {
        super.onCreate();

        bluetoothAdapter = bluetoothAdapter.getDefaultAdapter();

        toastTxt("BLTTH_SERVICE", "Create... ");
        Log.e("BLTTH_SERVICE", "Bluetooth name: " + bluetoothAdapter.getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("SERVICE", "onStartCommand: ... ");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopSelf();
        Log.e("SERVICE", "onDestroy: ... ");
        toastTxt("SERVICE_ONDESTROY", "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        Log.e("SERVICE", "onBind: ... ");

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

                // Discovery is resource intensive. Make sure it isn't going on when you attempt to connect and pass your message.
                bluetoothAdapter.cancelDiscovery();

                // Establish the connection. This will block until it connects.
                try {
                    bluetoothSocket.connect();
                } catch (IOException e) {
                    Log.d("BLTTH_SERVICE", "... Close socket during connection failure ...");
                } finally {
                    //initialBluetoothInOut();
                    iniciar();
                }

            }

        }).start();

        return bluetoothSocket;
    }

    public void iniciar() {

        // Reads bytes from this stream and stores them in the byte array
        final byte[] msgBuffer = new byte[1024];

        new Thread(new Runnable() {
            @Override
            public void run() {

                // Verifica se há conexão estabelecida com o Bluetooth.
                if (bluetoothSocket.isConnected()) {

                    try {

                        //outputStream = bluetoothSocket.getOutputStream();
                        inputStream = bluetoothSocket.getInputStream();

                        is = new DataInputStream(inputStream);
                        os = new DataOutputStream(bluetoothSocket.getOutputStream());

                        sharedPreferenceTerminal = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        sharedPreferenceEditor = sharedPreferenceTerminal.edit();

                        while (true) {

                            inputStream.read(msgBuffer);

                            sharedPreferenceEditor.putString("mssg", new String(msgBuffer));
                            sharedPreferenceEditor.putInt("mssg_code", 1);
                            sharedPreferenceEditor.apply();

                            Log.e("BLTTH_SERVICE", "iniciar().WHILE   ---> " + new String(msgBuffer));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Log.e("BLTTH_SERVICE", "iniciar().finally");
                    }
                }
            }
        }).start();

    }

    public void enviarComando(final String cmd) {

        sharedPreferenceEditor.putString("mssg", cmd);
        sharedPreferenceEditor.putInt("mssg_code", 0);
        sharedPreferenceEditor.apply();

        new Thread(new Runnable() {
            @Override
            public void run() {

                // Verifica se há conexão estabelecida com o Bluetooth.
                if(bluetoothSocket.isConnected()){

                    try{

                        //outputStream.write(msgBufferTwo);
                        os.writeUTF(cmd);

                        // SystemClock.sleep(3000);
                        Log.e("BLTTH_SERVICE", "Enviando comando: " + cmd);

                    } catch (Exception e) {
                        e.getMessage();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth não está conectado", Toast.LENGTH_LONG).show();
                }

            }
        }).start();

    }

    public void toastTxt(String tag,  String text) {
        Toast.makeText(this, tag + text, Toast.LENGTH_LONG).show();
    }

}