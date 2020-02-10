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
        String sql = "CREATE TABLE tb_board (" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
                ",nome TEXT" +
                ",descricao TEXT" +
                ",bluetooth_name TEXT" +
                ",mac_address TEXT" +
                ",rede TEXT" +
                ",ip TEXT" +
                ",conected_bluetooth INTEGER DEFAULT 0" +
                ",conected_wifi INTEGER DEFAULT 0" +
                ",created_at TEXT" +
                ",updated_at TEXT" +
                ");";
        db.execSQL(sql);

        String sql1 = "CREATE TABLE tb_button (" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
                ",nome TEXT" +
                ",cmd_on TEXT" +
                ",cmd_off TEXT" +
                ",cmd_flag TEXT" +
                ");";
        db.execSQL(sql1);
    }

    /**
     * Atualiza a estrutura da tabela no banco de dados, caso sua versão tenha mudado. */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS tb_board";
        db.execSQL(sql);
        String sql1 = "DROP TABLE IF EXISTS tb_button";
        db.execSQL(sql1);
        onCreate(db);
    }
}
