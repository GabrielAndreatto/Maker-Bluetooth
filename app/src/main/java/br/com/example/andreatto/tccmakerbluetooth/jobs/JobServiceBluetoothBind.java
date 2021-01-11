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
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceConnectionBluetoothBind;

@SuppressLint("NewApi")
public class JobServiceBluetoothBind extends JobService {
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
//        String mac_address = (String) jobP.getExtras().get("");
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

        return true; // Ainda há algo a ser executado ? true : false
    }

    @Override
    public boolean onStopJob(JobParameters jobP) {
        Log.e(LOG_PAGE, "onStop Job");
        return false; // jobParameters
    }
}
