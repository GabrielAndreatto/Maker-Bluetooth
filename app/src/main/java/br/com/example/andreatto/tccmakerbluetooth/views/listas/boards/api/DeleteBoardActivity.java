package br.com.example.andreatto.tccmakerbluetooth.views.listas.boards.api;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.dao.BoardDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Board;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.classes.Print;

public class DeleteBoardActivity extends AppCompatActivity {

    private Print print = new Print();
    private Bundle pacote = new Bundle();
    private String code;

    private Board board;
    private List<Board> boardList = new ArrayList<>();
    private BoardDAO boardDAO = new BoardDAO(this);
    private int boardId;
    private String nomeBoardDelete;
    private int idBoardDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pacote = getIntent().getExtras();
        if(!pacote.isEmpty() || getIntent().getExtras() != null) {

            code = pacote.getString("code");
            print.logE("CODE", code);

            if (code.contains("delete")) {
                boardId = pacote.getInt("board_id");
                board = getBoard();
                nomeBoardDelete = board.getNome();
                idBoardDelete = (int) board.getId();
            }
        }

        print.logE("Board", "Id: "+board.getId());
        print.logE("Board", "Nome: "+board.getNome());
        print.logE("Board", "Descrição: "+board.getDescricao());

        deleteBoard();

    }

    private Board getBoard() {
        boardList = boardDAO.all();
        Board boardT = new Board();
        for(Board boardTemp: boardList) {
            if(boardTemp.getId() == boardId) {
                boardT = boardTemp;
            }
        }
        return boardT;
    }

    private void deleteBoard() {
        try{
            boardDAO.remover(board);
        } catch (Exception e) {
            e.getMessage();
        } finally {
            finish();
        }
    }

}
