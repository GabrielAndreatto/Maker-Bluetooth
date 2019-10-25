package br.com.example.andreatto.tccmakerbluetooth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceConnectionBluetoothBind;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.activitys.AppCompatActivityBluetooth;

public class FirstActivity extends AppCompatActivityBluetooth {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initial();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void initial() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // SERVICE
                // 1 - Bluetooth
                Intent intent = new Intent(getApplicationContext(), ServiceBluetooth.class);
                startService(intent);
                // ServiceBluetooth systemService = getSystemService(ServiceBluetooth.class);

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
