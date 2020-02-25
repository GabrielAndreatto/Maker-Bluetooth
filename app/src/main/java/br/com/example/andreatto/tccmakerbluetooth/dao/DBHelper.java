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

    String iconsSensors =
            "INSERT INTO [tb_icons] ([name], [icon]) VALUES " +
                    "('Digital','ic_touch_light')," +
                    "('Clock','ic_clock_light')," +
                    "('Board','ic_developer_boards_light')," +
                    "('Dimmer','ic_dimmer_light')," +
                    "('Enviar','ic_send_dark')," +
                    "('Sensor','ic_sensor_light')," +
                    "('Timer','ic_timer_light')," +
                    "('Voice','ic_voice_light')," +
                    "('Wifi','ic_wifi_light')," +
                    "('Sol','ic_sol_light')," +
                    "('Lâmpada','ic_lamp_light')," +
                    "('Fumaça','ic_fume_light')," +
                    "('Fogo','ic_fire_light')," +
                    "('Umidade','ic_opacity_light')," +
                    "('Digital','ic_digital_light')," +
                    "('Relógio','ic_relogio_light')," +
                    "('Adicionar','ic_add_light')," +
                    "('Dastância','ic_distance_light')," +
                    "('Vento','ic_vento_light')," +
                    "('Luz','ic_luz_light')," +
                    "('Power','ic_power_light')," +
                    "('Umidade','ic_umidade_gota_custom_blue')";

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
                ",actived INTEGER DEFAULT 0" +
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

        String sql_icons = "CREATE TABLE tb_icons (" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
                ",name TEXT" +
                ",icon TEXT" +
                ");";
        db.execSQL(sql_icons);

        db.execSQL(iconsSensors);

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
        String sql_sensor_icons_drop = "DROP TABLE IF EXISTS tb_icons";
        db.execSQL(sql_sensor_icons_drop);

        onCreate(db);
    }
}
