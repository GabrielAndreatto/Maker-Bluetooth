package br.com.example.andreatto.tccmakerbluetooth.services.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import br.com.example.andreatto.tccmakerbluetooth.modelo.chat.MensagensPojo;
import br.com.example.andreatto.tccmakerbluetooth.modelo.chat.PojoMensagem;

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
    private SharedPreferences sharedPreferenceSensor;
    private SharedPreferences.Editor sharedPreferenceSensorEditor;

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothAdapter = bluetoothAdapter.getDefaultAdapter();
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

                bluetoothAdapter.cancelDiscovery();

                try {
                    bluetoothSocket.connect();
                } catch (IOException e) {
                    Log.d("BLTTH_SERVICE", "... Close socket during connection failure ...");
                } finally {
                    iniciarChat();
                }

            }

        }).start();

        return bluetoothSocket;
    }

    public void iniciarChat() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (bluetoothSocket.isConnected()) {
                    try {
                        //outputStream = bluetoothSocket.getOutputStream();
                        inputStream = bluetoothSocket.getInputStream();

                        is = new DataInputStream(inputStream);
                        os = new DataOutputStream(bluetoothSocket.getOutputStream());

                        sharedPreferenceTerminal = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        sharedPreferenceEditor = sharedPreferenceTerminal.edit();

                        while (true) {
                            if (inputStream.read() > 0) {
                                final byte[] msgBuffer = new byte[1024];
                                inputStream.read(msgBuffer);

                                sharedPreferenceEditor.putString("mensagem_chat", 1 + "-" + new String(msgBuffer));
                                sharedPreferenceEditor.apply();

                                Log.e("iniciar_chat", "Service ---> " + 1 + "-" + new String(msgBuffer));
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Log.e("iniciar_chat", "finally");
                    }
                }
            }
        }).start();
    }

    public void iniciarSensor(final String cmd) {

        enviarComando(cmd);

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (bluetoothSocket.isConnected()) {
                    try {

                        //outputStream = bluetoothSocket.getOutputStream();
                        inputStream = bluetoothSocket.getInputStream();

                        is = new DataInputStream(inputStream);
                        os = new DataOutputStream(bluetoothSocket.getOutputStream());

                        sharedPreferenceSensor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        sharedPreferenceSensorEditor = sharedPreferenceSensor.edit();

                        while (true) {
                            if (inputStream.read() > 0) {
                                final byte[] msgBuffer = new byte[1024];
                                inputStream.read(msgBuffer);

                                sharedPreferenceSensorEditor.putString("sensor", new String(msgBuffer));
                                sharedPreferenceSensorEditor.apply();

                                Log.e("iniciarSensor", "sensor ---> " + new String(msgBuffer));
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Log.e("iniciarSensor", "iniciarSensor().finally");
                    }
                }
            }
        }).start();

    }

    public void enviarComando(final String cmd) {

        sharedPreferenceEditor.putString("mensagem_chat", 0 + "-" + cmd);
        sharedPreferenceEditor.apply();

        new Thread(new Runnable() {
            @Override
            public void run() {

                // Verifica se há conexão estabelecida com o Bluetooth.
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

    public void enviarComandoSensor(final String cmd) {

        sharedPreferenceSensor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferenceSensorEditor = sharedPreferenceSensor.edit();

        new Thread(new Runnable() {
            @Override
            public void run() {

                // Verifica se há conexão estabelecida com o Bluetooth.
                if(bluetoothSocket.isConnected()){

                    try{
                        os.writeUTF(cmd);
                        SystemClock.sleep(250);

                        if (inputStream.read() > 0) {
                            final byte[] msgBuffer = new byte[1024];
                            inputStream.read(msgBuffer);

                            sharedPreferenceSensorEditor.putString("sensor", new String(msgBuffer));
                            sharedPreferenceSensorEditor.apply();

                            Log.e("iniciarSensor", "sensor ---> " + new String(msgBuffer));
                        }

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

    public String getValueSensor(final String cmd) throws UnsupportedEncodingException {
        Log.e("getValueSensor", "cmd: " +cmd);
        final byte[] msgBuffer = new byte[1024];
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    if (bluetoothSocket.isConnected()) {

                        os.writeUTF(cmd);

                        while (inputStream.read() > 0) {
                            Log.e("while", "inputStream");
                        }

                        if (inputStream.read() > 0) {
                            inputStream.read(msgBuffer);
                            Log.e("getValueSensor", "msg: "+new String(msgBuffer, "UTF-8"));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "BLuetooth Desconectado", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Log.e("getValueSensor", "finally");
                }

            }
        }).start();

        return new String(msgBuffer, "UTF-8");
    }


}