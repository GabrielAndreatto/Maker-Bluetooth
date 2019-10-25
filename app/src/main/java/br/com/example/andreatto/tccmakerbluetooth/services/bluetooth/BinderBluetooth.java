package br.com.example.andreatto.tccmakerbluetooth.services.bluetooth;

import android.os.Binder;

public class BinderBluetooth extends Binder {

    private ServiceBluetooth serviceBluetooth;

    public BinderBluetooth(ServiceBluetooth serviceBluetooth) {
        this.serviceBluetooth = serviceBluetooth;
    }

    /**
     * Método responsável por permitir o acesso ao Service. * @return
     */
    public ServiceBluetooth getServiceBluetooth() {
        return serviceBluetooth;
    }
}
