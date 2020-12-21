package br.com.example.andreatto.tccmakerbluetooth.views.listas.boards;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.BoardDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Board;
import br.com.example.andreatto.tccmakerbluetooth.views.form.board.BoardFormActivity;

public class BoardListActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private RecyclerBoardListAdapter adapter;
    private List<Board> boards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);

        initial();

    }

    public void initial() {
        BoardDAO boardDAO = new BoardDAO(this);
        boards = boardDAO.all();

        toolbar = findViewById(R.id.toolbar_board);
        // toolbar.setTitle("Lista de Board  (" + boards.size() + ") ");
        toolbar.setTitle("Boards");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView_board);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerBoardListAdapter(boards, this);
        recyclerView.setAdapter(adapter);
    }

    // Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_board, menu);
        return true;
    }

    // Toolbar item menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_menu:

                // Toast.makeText(this, "Adicionar", Toast.LENGTH_SHORT).show();
                Bundle pkg = new Bundle();
                pkg.putString("code", "adicionarToolbar");
                Intent i = new Intent(BoardListActivity.this, BoardFormActivity.class);
                i.putExtras(pkg);
                startActivity(i);
                break;

            case R.id.about_menu:
                Toast.makeText(this, "Sobre <implementando...>", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume", "onResume");
        initial();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {

            case 201:
                // Toast.makeText(getApplicationContext(), "Refresh", Toast.LENGTH_LONG).show();
                initial();
                break;

            case 202:
                Toast.makeText(getApplicationContext(), "bluetooth Mac Address", Toast.LENGTH_LONG).show();
                initial();
                break;
            case 0101:
                // Toast.makeText(getApplicationContext(), "activity result", Toast.LENGTH_LONG).show();
                initial();
                break;

            case 0102:
                Bundle pkg = intent.getExtras().getBundle("bluetooth_conection");
                if (pkg.getString("response").contains("ok")) {
                    Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();
                    initial();
                } else if (pkg.getString("response").contains("not")) {
                    Toast.makeText(getApplicationContext(), "Deconectado", Toast.LENGTH_LONG).show();
                    initial();
                } else {
                    Toast.makeText(getApplicationContext(), "Informações faltando, conferir cadastro do board", Toast.LENGTH_LONG).show();
                    initial();
                }
                break;

        }

    }
}
