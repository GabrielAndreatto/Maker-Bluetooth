package br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.teste;

import android.os.Binder;

import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;

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