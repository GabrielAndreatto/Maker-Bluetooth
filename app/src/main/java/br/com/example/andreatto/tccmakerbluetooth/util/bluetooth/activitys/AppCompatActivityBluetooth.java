package br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.activitys;

import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AppCompatActivityBluetooth extends AppCompatActivity {

    public SharedPreferences sPUserConf;
    protected BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bluetoothAdapter = bluetoothAdapter.getDefaultAdapter();
        // sPUserConf = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
    }

    public boolean verifyBluetoothAdapter() {
        return bluetoothAdapter != null;
    }

    public boolean enableBluetooth() {
        return bluetoothAdapter.isEnabled();
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

}
