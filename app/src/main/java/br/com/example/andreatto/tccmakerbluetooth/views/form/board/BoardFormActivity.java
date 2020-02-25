package br.com.example.andreatto.tccmakerbluetooth.views.form.board;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.BoardDAO;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.classes.Print;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.bluetoothList.BluetoothListActivityForActivateForm;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Board;

public class BoardFormActivity extends AppCompatActivity {

    private Board board;
    private int boardId = 0;
    private String statusIntentAction;

    private Toolbar toolbar;
    private Button btnSalvar;
    private AppCompatEditText editTextNome;
    private AppCompatEditText editTextDescricao;
    private AppCompatEditText editTextBluetoothName;
    private AppCompatEditText editTextMacAddress;
    private AppCompatEditText editTextRede;
    private AppCompatEditText editTextIp;

    private ImageView bluetooth;
    private ImageView wifi;

    Print print = new Print();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_form);

        if(getIntent().getExtras().isEmpty() || getIntent().getExtras() == null ) {
            board = new Board();
            initilizeView();
            listenerActions();
        } else {

            Bundle pacote = getIntent().getExtras();
            statusIntentAction = pacote.getString("code"); // enviado a partir da toolbar
            Log.e("CODE", "Params ... " + statusIntentAction);

            if (pacote.getString("code").contains("editar")) {

                boardId = pacote.getInt("board_id");
                getBoard();

                initilizeView();
                setValuesInitilizeView();
                listenerActions();

            }

            if(pacote.getString("code").contains("adicionar")) {
                board = new Board();
                initilizeView();
                listenerActions();
            }

        }

    }

    public void getBoard() {

        List<Board> boardList = new ArrayList<Board>();
        BoardDAO boardDAO = new BoardDAO(this);
        boardList = boardDAO.listaTodos();

        for (Board tmpBoard: boardList) {
            if(tmpBoard.getId() == boardId) {
                board = tmpBoard;
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private void setValuesInitilizeView() {
        editTextNome.setText(this.board.getNome());
        editTextDescricao.setText(this.board.getDescricao());
        editTextBluetoothName.setText(this.board.getBluetoothName());
        editTextMacAddress.setText(this.board.getMacAddress());
        editTextRede.setText(this.board.getRede());
        editTextIp.setText(this.board.getIp());

    }

    private void initilizeView() {

        toolbar = findViewById(R.id.toolbar_board);
        toolbar.setTitle("Cadastro de Board");
        setSupportActionBar(toolbar);

        btnSalvar = (Button) findViewById(R.id.btn_salvar);
        editTextNome = (AppCompatEditText) findViewById(R.id.cadastro_board_nome);
        editTextDescricao = (AppCompatEditText) findViewById(R.id.cadastro_board_descricao);
        editTextBluetoothName = (AppCompatEditText) findViewById(R.id.cadastro_board_bluetooth_name);
        editTextMacAddress = (AppCompatEditText) findViewById(R.id.cadastro_board_mac_addres);
        editTextRede = (AppCompatEditText) findViewById(R.id.cadastro_board_ip_nome);
        editTextIp = (AppCompatEditText) findViewById(R.id.cadastro_board_ip);

        bluetooth = (ImageView) findViewById(R.id.img_bluetooth);
        wifi = (ImageView) findViewById(R.id.img_wifi);
    }

    private void listenerActions() {
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                board.setNome(editTextNome.getText().toString());
                board.setDescricao(editTextDescricao.getText().toString());
                board.setBluetoothName(editTextBluetoothName.getText().toString());
                board.setMacAddress(editTextMacAddress.getText().toString());
                board.setRede(editTextRede.getText().toString());
                board.setIp(editTextIp.getText().toString());

                BoardDAO boardDAO = new BoardDAO(BoardFormActivity.this);
                boardDAO.salvar(board);

                if(boardId == 0) {

                    if(statusIntentAction.contains("adicionarToolbar")) {
                        Toast.makeText(getApplicationContext(), "Board criado com sucesso!", Toast.LENGTH_LONG) .show();
                        finish(); //
                    } else {
                        Toast.makeText(getApplicationContext(), "Board criado com sucesso!!", Toast.LENGTH_LONG) .show();
                        finish();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Board atualizado com sucesso!", Toast.LENGTH_LONG) .show();
                    finish(); //
                }

            }
        });

        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), BluetoothListActivityForActivateForm.class), 0101);
            }
        });

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BoardFormActivity.this, "Wifi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    // Toolbar item menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.about_menu:
                Toast.makeText(this, "Sobre <implementando...>", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case 0101:
                Bundle pkg = data.getExtras();
                editTextBluetoothName.setText(pkg.getString("nome"));
                editTextMacAddress.setText(pkg.getString("mac_address"));
                break;
           default:

        }
    }
}
