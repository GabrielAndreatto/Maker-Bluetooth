package br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.classes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AgileTools {

    public String getFullDateHour() {
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date data = new Date();
        String dataFormatada = formataData.format(data);
        return dataFormatada;
    }

    public String getFullHourDate() {
        SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date data = new Date();
        String dataFormatada = formataData.format(data);
        return dataFormatada;
    }

    public String getSubString(String msg, int first, int last) {
        String s;
        try {
            s = msg.substring(first, last);
        } catch (Exception e) {
            s = "Error: "+e.getMessage();
        }
        return s;
    }

    public String[] getSplit(String msg, String condition) {
        String[] s = new String[0];
        try {
            s = msg.split(condition);
        } catch (Exception e) {
            s[0] = e.getMessage();
        }
        return s;
    }

}