package br.com.example.andreatto.tccmakerbluetooth.services.bluetooth;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class ServiceConnectionBluetoothBind implements ServiceConnection {

    private static final String LOG_PAGE = "ServiceConBleBind";
    private ServiceBluetooth serviceBluetooth;

    // Estes métodos serão executados quando algum componente
    // se conectar e desconectar do Service, respectivamente;
    @Override
    public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
        BinderBluetooth binderBluetooth = (BinderBluetooth) serviceBinder;
        this.serviceBluetooth = binderBluetooth.getServiceBluetooth();
        Log.e(LOG_PAGE, "onServiceConnected(): ... ");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        // Tarefas a serem executadas ao se desconectar do Service
        Log.e(LOG_PAGE, "onServiceDisconnected(): ... ");
    }

    public ServiceBluetooth getServiceBluetooth() {
        if (this.serviceBluetooth != null) {
            Log.e(LOG_PAGE, "getServiceBluetooth(): ... ");
        } else {
            Log.e(LOG_PAGE, " ERROR getServiceBluetooth(): ... ");
        }
        return this.serviceBluetooth;
    }
}
