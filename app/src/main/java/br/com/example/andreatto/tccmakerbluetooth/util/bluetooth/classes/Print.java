package br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.classes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Print {
    public void toast(Context context, String msg, Boolean longTime) {
        if(longTime)  {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void logE(String tag, String message) {
        Log.e(tag, message);
    }
}
