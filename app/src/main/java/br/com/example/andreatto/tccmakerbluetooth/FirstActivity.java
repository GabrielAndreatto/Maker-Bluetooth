package br.com.example.andreatto.tccmakerbluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.activitys.AppCompatActivityBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.classes.Print;

public class FirstActivity extends AppCompatActivityBluetooth {

    Print print = new Print();
    public static final int CODE_ACTIVATION_BLUETOOTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!verifyBluetoothAdapter()){
            print.toast(getApplicationContext(), "Dispositivo não possui Bluetooth", true);
            finish();
        } else {
            isEnable();
        }
    }

    public void isEnable() {
        if(enableBluetooth()){
            goMainActivity();
        } else {
            startBluetoothDevice();
        }
    }

    public void startBluetoothDevice() {
        Intent enablelntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enablelntent, CODE_ACTIVATION_BLUETOOTH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case CODE_ACTIVATION_BLUETOOTH:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(getApplicationContext(), "Bluetooth foi ativado", Toast.LENGTH_LONG).show();
                    goMainActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth não foi ativado", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    private void goMainActivity() {
        initialService();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void initialService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // SERVICE
                // 1 - Bluetooth
                if (!enableBluetooth()) {
                    Intent intent = new Intent(getApplicationContext(), ServiceBluetooth.class);
                    startService(intent);
                }
                // ServiceBluetooth systemService = getSystemService(ServiceBluetooth.class);

            }
        }).start();
    }

}
