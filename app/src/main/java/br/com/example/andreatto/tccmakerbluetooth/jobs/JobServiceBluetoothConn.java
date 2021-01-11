package br.com.example.andreatto.tccmakerbluetooth.jobs;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceConnectionBluetoothBind;

@SuppressLint("NewApi")
public class JobServiceBluetoothConn extends JobService {
    private static final String LOG_PAGE = "JobServiceBleConn";
    protected BluetoothAdapter bluetoothAdapter;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothDevice bluetoohRemoto;
    private BluetoothSocket bluetoothSocket = null;
    private BluetoothServerSocket serverSocket;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;
    private DataInputStream is;
    private DataOutputStream os;

    // Preferences
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor sharedPreferenceEditor;

    // SERVICE
    // 1 - Bluetooth
    private ServiceBluetooth serviceBluetooth;
    private Intent intentService;
    private ServiceConnectionBluetoothBind serviceConnection;

    @Override
    public boolean onStartJob(JobParameters jobP) {
        //jobFinished(jobP,false);
        Log.e(LOG_PAGE, "onStart Job");
//        bluetoothAdapter = bluetoothAdapter.getDefaultAdapter();
////        String mac_address = (String) jobP.getExtras().get("");
//        String mac_address = "98:D3:31:F5:27:B6";
//        conectarBluetooth(mac_address);

        // SERVICE  1 - Bluetooth
        intentService = new Intent(this, ServiceBluetooth.class);
        serviceConnection = new ServiceConnectionBluetoothBind();
        try {
            bindService(intentService, serviceConnection, 0);
            Log.e(LOG_PAGE, "SUCCESS bind Service");
        } catch (Exception e) {
            Log.e(LOG_PAGE, "ERROR bind Service");
        }

        return true; // Ainda há algo a ser executado?
    }

    @Override
    public boolean onStopJob(JobParameters jobP) {
        Log.e(LOG_PAGE, "onStop Job");
        return false; // jobParameters
    }

    public void sendMessage() {
        Log.e(LOG_PAGE, "send Message Job");
    }

    public void conectarBluetooth(String mac_address) {
        Log.e(LOG_PAGE, "conectar Bluetooth mac_address " + mac_address);
        this.bluetoothSocket = conectBluetooth(mac_address);
    }

    public BluetoothSocket conectBluetooth(String mac_address) {

        Log.e(LOG_PAGE, "conect Bluetooth: " + mac_address);

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

                                sendCommand("a");
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
                if (bluetoothSocket.isConnected()) {
                    try {
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
