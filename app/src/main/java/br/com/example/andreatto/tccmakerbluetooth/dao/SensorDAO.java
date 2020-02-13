package br.com.example.andreatto.tccmakerbluetooth.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.modelo.Sensor;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.classes.UtilSimpleTools;

public class SensorDAO {

    private DBHelper dbHelper;
    private Context context;

    public SensorDAO(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void save(Sensor sensor) {
        if ((Long)sensor.getId() == null || sensor.getId() == 0) {
            add(sensor);
        } else {
            update(sensor);
        } 
    }

    public void add(Sensor sensor) {
        ContentValues values = new ContentValues();
        values.put("name", sensor.getName());
        values.put("command", sensor.getCommand());
        values.put("value", sensor.getValue());
        values.put("icon", sensor.getIcon());
        values.put("used_at", " IS NULL");
        values.put("created_at", new UtilSimpleTools().getHoraDateFullCelular());
        values.put("updated_at", " IS NULL");

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert("tb_sensor", null, values);
        sensor.setId((int) id);
        db.close();
    }

    public List<Sensor> all() {
        List<Sensor> sensors = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("tb_sensor", null, null, null, null, null, "_id ASC");
        try{
            while(c.moveToNext()) {
                Sensor sensor = new Sensor();
                sensor.setId(c.getLong(c.getColumnIndex("_id")));
                sensor.setName(c.getString(c.getColumnIndex("name")));
                sensor.setCommand(c.getString(c.getColumnIndex("command")));
                sensor.setValue(c.getString(c.getColumnIndex("value")));
                sensor.setIcon(c.getString(c.getColumnIndex("icon")));
                sensor.setUsed_at(c.getString(c.getColumnIndex("used_at")));
                sensor.setCreated_at(c.getString(c.getColumnIndex("created_at")));
                sensor.setUpdated_at(c.getString(c.getColumnIndex("updated_at")));
                sensors.add(sensor);
            }
        }
        finally {
            c.close();
        }
        db.close();
        return sensors;
    }

    public Sensor getSensorWithId(String idSensor) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Sensor sensor = new Sensor();
        List<Sensor> sensors = this.all();

        for (Sensor tmpSensor: sensors) {
            if(String.valueOf(tmpSensor.getId()) == idSensor) {
                sensor = tmpSensor;
            }
        }
        db.close();
        return sensor;

    }

    public void update(Sensor sensor) {
        ContentValues values = new ContentValues();
        values.put("name", sensor.getName());
        values.put("command", sensor.getCommand());
        values.put("value", sensor.getValue());
        values.put("icon", sensor.getIcon());
        values.put("used_at", sensor.getUsed_at());
        // values.put("created_at", new UtilSimpleTools().getHoraDateFullCelular());
        values.put("updated_at", new UtilSimpleTools().getHoraDateFullCelular());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update("tb_sensor", values, "_id=?", new String[] {String.valueOf(sensor.getId())});
        db.close();
    }

    public void updateSensorValue(Sensor sensor) {
        ContentValues values = new ContentValues();

        Log.e("SENSOR", "USED: "+sensor.getUsed_at());

        values.put("value", sensor.getValue());
        values.put("used_at", sensor.getUsed_at());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update("tb_sensor", values, "_id=?", new String[] {String.valueOf(sensor.getId())});
        db.close();
    }

    public void remover(Sensor sensor) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("tb_sensor", "_id=?", new String[] {String.valueOf(sensor.getId())});
        db.close();
    }
}
