package br.com.example.andreatto.tccmakerbluetooth.services.bluetooth;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class IntentServiceBluetooth extends IntentService {

    private static final String TAG = "";
    private boolean running;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public IntentServiceBluetooth(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        running = true;
        int count = 0;

        while(running && count < 100) {
            fazAlgumaCoisa();
            Log.d(TAG, "ExemploServico executando... " + count);
            count++;
        }

        Log.d(TAG, "FIM ... " + count);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
        Log.d(TAG, "on Destroy... ");
    }

    private void fazAlgumaCoisa() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.getMessage();
        }
    }


} // fim class