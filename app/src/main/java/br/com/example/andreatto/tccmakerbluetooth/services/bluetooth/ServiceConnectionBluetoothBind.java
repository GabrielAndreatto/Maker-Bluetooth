package br.com.example.andreatto.tccmakerbluetooth.services.bluetooth;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class ServiceConnectionBluetoothBind implements ServiceConnection {

    private ServiceBluetooth serviceBluetooth;

    // Estes métodos serão executados quando algum componente
    // se conectar e desconectar do Service, respectivamente;
    @Override
    public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
        BinderBluetooth binderBluetooth = (BinderBluetooth) serviceBinder;
        this.serviceBluetooth = binderBluetooth.getServiceBluetooth();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        // Tarefas a serem executadas ao se desconectar do Service
    }

    public ServiceBluetooth getServiceBluetooth() {
        return serviceBluetooth;
    }

}
