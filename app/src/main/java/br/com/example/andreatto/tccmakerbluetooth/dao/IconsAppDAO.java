package br.com.example.andreatto.tccmakerbluetooth.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.modelo.IconsApp;

public class IconsAppDAO {

    private DBHelper dbHelper;
    private Context context;

    public IconsAppDAO(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void save(IconsApp iconsSensor) {
        if (iconsSensor.getId() == 0) {
            add(iconsSensor);
        } else {
            update(iconsSensor);
        } 
    }

    public void add(IconsApp iconsSensor) {
        ContentValues values = new ContentValues();
        values.put("name", iconsSensor.getName());
        values.put("icon", iconsSensor.getIcon());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert("tb_icons", null, values);
        iconsSensor.setId((int) id);
        db.close();
    }

    public List<IconsApp> all() {
        List<IconsApp> iconsSensors = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("tb_icons", null, null, null, null, null, "_id ASC");
        try{
            while(c.moveToNext()) {
                IconsApp iconsSensorT = new IconsApp();
                iconsSensorT.setId(c.getColumnIndex("_id"));
                iconsSensorT.setName(c.getString(c.getColumnIndex("name")));
                iconsSensorT.setIcon(c.getString(c.getColumnIndex("icon")));
                iconsSensors.add(iconsSensorT);
            }
        }
        finally {
            c.close();
        }
        db.close();
        return iconsSensors;
    }

    public IconsApp getIconWithId(String idSensorIcon) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        IconsApp iconsSensor = new IconsApp();
        List<IconsApp> sensorsList = this.all();

        for (IconsApp tmpSensor: sensorsList) {
            if(String.valueOf(tmpSensor.getId()) == idSensorIcon) {
                iconsSensor = tmpSensor;
            }
        }
        db.close();
        return iconsSensor;
    }

    public void update(IconsApp iconsSensor) {
        ContentValues values = new ContentValues();
        values.put("name", iconsSensor.getName());
        values.put("icon", iconsSensor.getIcon());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update("tb_icons", values, "_id=?", new String[] {String.valueOf(iconsSensor.getId())});
        db.close();
    }

    public void updateSensorValue(IconsApp iconsSensor) {
        ContentValues values = new ContentValues();

        values.put("name", iconsSensor.getName());
        values.put("icon", iconsSensor.getIcon());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update("tb_icons", values, "_id=?", new String[] {String.valueOf(iconsSensor.getId())});
        db.close();
    }

    public void remover(IconsApp iconsSensor) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("tb_icons", "_id=?", new String[] {String.valueOf(iconsSensor.getId())});
        db.close();
    }
}
