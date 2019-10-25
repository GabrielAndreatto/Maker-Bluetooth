package br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.classes;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtilSimpleTools {

    public String getHoraDateFullCelular() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        Date data = new Date();

        String data_completa = dateFormat.format(data);

        //Log.i("data_completa", data_completa);

        return data_completa;
    }

    public String getHoraCelular() {
        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

        Date data = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        String hora_atual = dateFormat_hora.format(data_atual);

        return hora_atual;
    }

    public String getDateCelular() {
        SimpleDateFormat dateFormat_date_day = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        String data_completa = dateFormat_date_day.format(data_atual);

        return data_completa;
    }

}
