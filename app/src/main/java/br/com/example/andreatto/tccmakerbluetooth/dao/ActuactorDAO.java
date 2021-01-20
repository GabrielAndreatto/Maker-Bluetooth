package br.com.example.andreatto.tccmakerbluetooth.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.modelo.Actuator;
import br.com.example.andreatto.tccmakerbluetooth.utils.classes.UtilSimpleTools;

public class ActuactorDAO {

    private DBHelper dbHelper;
    private Context context;

    public ActuactorDAO(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void save(Actuator actuator) {
        if (actuator.getId() == 0) {
            add(actuator);
        } else {
            update(actuator);
        } 
    }

    public void add(Actuator actuator) {
        ContentValues values = new ContentValues();
        values.put("name", actuator.getName());
        values.put("cmd_on", actuator.getCommandOn());
        values.put("cmd_off", actuator.getCommandOff());
        values.put("type", actuator.getTipo());
        values.put("actived", actuator.getActived());
        values.put("used_at", " IS NULL");
        values.put("created_at", new UtilSimpleTools().getHoraDateFullCelular());
        values.put("updated_at", " IS NULL");

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert("tb_actuator", null, values);
        actuator.setId((int) id);
        db.close();
    }

    public List<Actuator> all() {
        List<Actuator> actuators = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("tb_actuator", null, null, null, null, null, "_id ASC");
        try{
            while(c.moveToNext()) {
                Actuator actuator = new Actuator();
                actuator.setId((int) c.getLong(c.getColumnIndex("_id")));
                actuator.setName(c.getString(c.getColumnIndex("name")));
                actuator.setCommandOn(c.getString(c.getColumnIndex("cmd_on")));
                actuator.setCommandOff(c.getString(c.getColumnIndex("cmd_off")));
                actuator.setTipo(c.getString(c.getColumnIndex("type")));
                actuator.setActived(c.getInt(c.getColumnIndex("actived")));
                actuator.setUsed_at(c.getString(c.getColumnIndex("used_at")));
                actuator.setCreated_at(c.getString(c.getColumnIndex("created_at")));
                actuator.setUpdated_at(c.getString(c.getColumnIndex("updated_at")));
                actuators.add(actuator);
            }
        }
        finally {
            c.close();
        }
        db.close();
        return actuators;
    }

    public List<Actuator> allToggle() {
        List<Actuator> actuators = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("tb_actuator", null, "type='toggle'", null, null, null, "_id ASC");
        try{
            while(c.moveToNext()) {
                Actuator actuator = new Actuator();
                actuator.setId((int) c.getLong(c.getColumnIndex("_id")));
                actuator.setName(c.getString(c.getColumnIndex("name")));
                actuator.setCommandOn(c.getString(c.getColumnIndex("cmd_on")));
                actuator.setCommandOff(c.getString(c.getColumnIndex("cmd_off")));
                actuator.setTipo(c.getString(c.getColumnIndex("type")));
                actuator.setActived(c.getInt(c.getColumnIndex("actived")));
                actuator.setUsed_at(c.getString(c.getColumnIndex("used_at")));
                actuator.setCreated_at(c.getString(c.getColumnIndex("created_at")));
                actuator.setUpdated_at(c.getString(c.getColumnIndex("updated_at")));
                actuators.add(actuator);
            }
        }
        finally {
            c.close();
        }
        db.close();
        return actuators;
    }

    public List<Actuator> allOnoff() {
        List<Actuator> actuators = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("tb_actuator", null, "type='onoff'", null, null, null, "_id ASC");
        try{
            while(c.moveToNext()) {
                Actuator actuator = new Actuator();
                actuator.setId((int) c.getLong(c.getColumnIndex("_id")));
                actuator.setName(c.getString(c.getColumnIndex("name")));
                actuator.setCommandOn(c.getString(c.getColumnIndex("cmd_on")));
                actuator.setCommandOff(c.getString(c.getColumnIndex("cmd_off")));
                actuator.setTipo(c.getString(c.getColumnIndex("type")));
                actuator.setActived(c.getInt(c.getColumnIndex("actived")));
                actuator.setUsed_at(c.getString(c.getColumnIndex("used_at")));
                actuator.setCreated_at(c.getString(c.getColumnIndex("created_at")));
                actuator.setUpdated_at(c.getString(c.getColumnIndex("updated_at")));
                actuators.add(actuator);
            }
        }
        finally {
            c.close();
        }
        db.close();
        return actuators;
    }

    public Actuator getSensorWithId(String idActuator) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Actuator actuator = new Actuator();
        List<Actuator> actuators = this.all();

        for (Actuator tmpActuator: actuators) {
            if(String.valueOf(tmpActuator.getId()) == idActuator) {
                actuator = tmpActuator;
            }
        }
        db.close();
        return actuator;

    }

    public void update(Actuator actuator) {
        ContentValues values = new ContentValues();
        values.put("name", actuator.getName());
        values.put("cmd_on", actuator.getCommandOn());
        values.put("cmd_off", actuator.getCommandOff());
        values.put("type", actuator.getTipo());
        values.put("actived", actuator.getActived());
        values.put("used_at", actuator.getUsed_at());
        values.put("updated_at", new UtilSimpleTools().getHoraDateFullCelular());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update("tb_actuator", values, "_id=?", new String[] {String.valueOf(actuator.getId())});
        db.close();
    }

    public void updateCommand(Actuator actuator) {
        ContentValues values = new ContentValues();
        values.put("actived", actuator.getActived());
        values.put("used_at", actuator.getUsed_at());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update("tb_actuator", values, "_id=?", new String[] {String.valueOf(actuator.getId())});
        db.close();
    }

    public void remover(Actuator actuator) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("tb_actuator", "_id=?", new String[] {String.valueOf(actuator.getId())});
        db.close();
    }

    public void removerWithId(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("tb_actuator", "_id=?", new String[] {String.valueOf(id)});
        db.close();
    }
}
