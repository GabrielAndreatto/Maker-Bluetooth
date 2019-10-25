package br.com.example.andreatto.tccmakerbluetooth.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.modelo.Board;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.classes.UtilSimpleTools;

public class BoardDAO {

    private DBHelper dbHelper;
    private Context context;
    
    public BoardDAO(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }
    
    /**
     * Salva board no banco de dados.
     * Caso o board não exista no banco de dados, ele o adiciona.
     * Caso o board exista no banco de dados, apenas atualiza os valores dos campos modificados. *
     * @param board */
    
    public void salvar(Board board) {
        /**
         * Se o ID do board é nulo é porque ele ainda não existe no banco de dados, * logo subentende-se que queremos adicionar o board no banco de dados. * Sendo assim, chamaremos o método adiciona() já definido no DAO.
         */

        if ((Long)board.getId() == null || board.getId() == 0) {
            adicionar(board);
        /**
         * Caso o board possua um ID é porque ele já existe no banco de dados, logo * subentende-se que queremos alterar seus dados no banco de dados.
         * Sendo assim, chamaremos o método atualiza() já definido no DAO.
         */
        } else {
            atualizar(board);
        } 
    }

    /**
     * Adiciona board no banco de dados. */
    public void adicionar(Board board) {

        //String date = new UtilSimpleTools().getHoraDateFullCelular();
        // Encapsula no board do tipo ContentValues os
        // valores a serem persistidos no banco de dados
        ContentValues values = new ContentValues();
        values.put("nome", board.getNome());
        values.put("descricao", board.getDescricao());
        values.put("bluetooth_name", board.getBluetoothName());
        values.put("mac_address", board.getMacAddress());
        values.put("rede", board.getRede());
        values.put("ip", board.getIp());
        //values.put("conected_bluetooth", board.getConectedBluetooth());
        //values.put("conected_wifi", board.getConectedWifi());
        values.put("created_at", new UtilSimpleTools().getHoraDateFullCelular());
        values.put("updated_at", " IS NULL");

        // Instancia uma conexão com o banco de dados, em modo de gravação
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Insere o registro no banco de dados
        long id = db.insert("tb_board", null, values);
        board.setId((int) id);

        // Encerra a conexão com o banco de dados
        db.close(); 
    }

    /**
     * Lista todos os registros da tabela “objeto_emprestado” */
    public List<Board> listaTodos() {
        // Cria um List para guardar os boards consultados no banco de dados
        List<Board> boards = new ArrayList<Board>();

        // Instancia uma nova conexão com o banco de dados em modo leitura
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Executa a consulta no banco de dados
        Cursor c = db.query("tb_board", null, null, null, null, null, "_id ASC");

        try{
            while(c.moveToNext()) {
                Board obj = new Board();

                obj.setId(c.getLong(c.getColumnIndex("_id")));

                obj.setNome(c.getString(c.getColumnIndex("nome")));
                obj.setDescricao(c.getString(c.getColumnIndex("descricao")));

                obj.setBluetoothName(c.getString(c.getColumnIndex("bluetooth_name")));
                obj.setMacAddress(c.getString(c.getColumnIndex("mac_address")));

                obj.setRede(c.getString(c.getColumnIndex("rede")));
                obj.setIp(c.getString(c.getColumnIndex("ip")));

                obj.setConectedBluetooth(c.getInt(c.getColumnIndex("conected_bluetooth")));
                obj.setConectedWifi(c.getInt(c.getColumnIndex("conected_wifi")));

                obj.setCreated_at(c.getString(c.getColumnIndex("created_at")));
                obj.setUpdated_at(c.getString(c.getColumnIndex("updated_at")));

                boards.add(obj);
            }
        }
        finally {
            // encerra o cursor
            c.close();
        }

        // Encerra a conexão com o banco de dados
        db.close();
        // Retorna uma lista com os boards consultados
        return boards;

    }


    /**
     * Lista todos os registros da tabela “objeto_emprestado” */
    public Board getBoardWithId(String idBoard) {

        // Instancia uma nova conexão com o banco de dados em modo leitura
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Board obj = new Board();
        List<Board> boards = this.listaTodos();

        for (Board tmpBoard: boards) {
            if(String.valueOf(tmpBoard.getId()) == idBoard) {
                obj = tmpBoard;
            }
        }

        // Encerra a conexão com o banco de dados
        db.close();
        // Retorna uma lista com os boards consultados
        return obj;

    }

    /**
     * Altera o registro no banco de dados. */
    public void atualizar(Board board) {

        //String date = new UtilSimpleTools().getHoraDateFullCelular();
        // Encapsula no board do tipo ContentValues os valores

        // a serem atualizados no banco de dados
        ContentValues values = new ContentValues();
        values.put("nome", board.getNome());
        values.put("descricao", board.getDescricao());
        values.put("bluetooth_name", board.getBluetoothName());
        values.put("mac_address", board.getMacAddress());
        values.put("rede", board.getRede());
        values.put("ip", board.getIp());
        // values.put("conected_bluetooth", board.getConectedBluetooth());
        // values.put("conected_wifi", board.getConectedWifi());
        //values.put("created_at", System.currentTimeMillis());
        values.put("updated_at", new UtilSimpleTools().getHoraDateFullCelular());

        // Instancia uma conexão com o banco de dados, em modo de gravação
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Atualiza o registro no banco de dados
        db.update("tb_board", values, "_id=?", new String[] {String.valueOf(board.getId())});
        // Encerra a conexão com o banco de dados
        db.close();
    }

    /**
     * Altera o registro no banco de dados. */
    public void
    atualizarBluetoothBoard(String board, String mac_address, String name) {
        // Encapsula no board do tipo ContentValues os valores
        // a serem atualizados no banco de dados
        ContentValues values = new ContentValues();
        values.put("bluetooth_name", name);
        values.put("mac_address", mac_address);

        // Instancia uma conexão com o banco de dados, em modo de gravação
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Atualiza o registro no banco de dados
        db.update("tb_board", values, "_id=?", new String[] {String.valueOf(board)});
        // Encerra a conexão com o banco de dados
        db.close();
    }

    /**
     * Altera o registro no banco de dados. */
    public void conexBluetoothFromBoard(String board) {

        // Encapsula no board do tipo ContentValues os valores
        // a serem atualizados no banco de dados
        ContentValues values = new ContentValues();
        values.put("conected_bluetooth", 1); // precisa desconectar

        // Instancia uma conexão com o banco de dados, em modo de gravação
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Atualiza o registro no banco de dados
        db.update("tb_board", values, "_id=?", new String[] {String.valueOf(board)});
        // Encerra a conexão com o banco de dados
        db.close();

    }


    /**
     * Altera o registro no banco de dados. */
    public void desconexBluetoothFromBoard(String board) {

        // Encapsula no board do tipo ContentValues os valores
        // a serem atualizados no banco de dados
        ContentValues values = new ContentValues();
        values.put("conected_bluetooth", 0);

        // Instancia uma conexão com o banco de dados, em modo de gravação
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Atualiza o registro no banco de dados
        db.update("tb_board", values, "_id=?", new String[] {String.valueOf(board)});
        // Encerra a conexão com o banco de dados
        db.close();

    }



    public void remover(Board board) {

        // Instancia uma conexão com o banco de dados, em modo de gravação
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete("tb_board", "_id=?", new String[] {String.valueOf(board.getId())} );
        db.close();
    }
}
