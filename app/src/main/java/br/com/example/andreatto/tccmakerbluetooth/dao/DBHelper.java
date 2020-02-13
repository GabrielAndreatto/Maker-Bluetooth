package br.com.example.andreatto.tccmakerbluetooth.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    // Nome do banco de dados
    private static final String NOME_DO_BANCO = "db_maker";
    // Versão atual do banco de dados
    private static final int VERSAO_DO_BANCO = 1;

    public DBHelper(Context context) {
        super(context, NOME_DO_BANCO, null, VERSAO_DO_BANCO);
    }

    /**
     * Cria a tabela no banco de dados, caso ela não exista.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_board = "CREATE TABLE tb_board (" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
                ",name TEXT" +
                ",description TEXT" +
                ",bluetooth_name TEXT" +
                ",mac_address TEXT" +
                ",rede TEXT" +
                ",ip TEXT" +
                ",conected_bluetooth INTEGER DEFAULT 0" +
                ",conected_wifi INTEGER DEFAULT 0" +
                ",used_at TEXT" +
                ",created_at TEXT" +
                ",updated_at TEXT" +
                ");";
        db.execSQL(sql_board);

        String sql_actuators = "CREATE TABLE tb_actuator (" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
                ",name TEXT" +
                ",cmd_on TEXT" +
                ",cmd_off TEXT" +
                ",type TEXT" +
                ",used_at TEXT" +
                ",created_at TEXT" +
                ",updated_at TEXT" +
                ");";
        db.execSQL(sql_actuators);

        String sql_sensors = "CREATE TABLE tb_sensor (" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
                ",name TEXT" +
                ",command TEXT" +
                ",value TEXT" +
                ",icon TEXT" +
                ",used_at TEXT" +
                ",created_at TEXT" +
                ",updated_at TEXT" +
                ");";
        db.execSQL(sql_sensors);
    }

    /**
     * Atualiza a estrutura da tabela no banco de dados, caso sua versão tenha mudado. */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql_board_drop = "DROP TABLE IF EXISTS tb_board";
        db.execSQL(sql_board_drop);
        String sql_actuator_drop = "DROP TABLE IF EXISTS tb_actuator";
        db.execSQL(sql_actuator_drop);
        String sql_sensor_drop = "DROP TABLE IF EXISTS tb_sensor";
        db.execSQL(sql_sensor_drop);
        onCreate(db);
    }
}
