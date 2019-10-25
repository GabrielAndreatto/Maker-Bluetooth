package br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.activitys;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import br.com.example.andreatto.tccmakerbluetooth.dao.BoardDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Board;

public class AppCompatActivityBluetoothCon extends AppCompatActivityBluetooth {

    private List<Board> boards;
    private Board boardSend, boardConected;
    private Activity activity;
    private int id_board;
    private int id_board_conected = 0;

    private BluetoothDevice device;
    private BluetoothSocket btSocket = null;
    private InputStream inputStream = null;
    private OutputStream outStream = null;

    // Well known SPP UUID
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private boolean statusSocket = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras() == null) {
            returnActivityOff();
        } else {

            if (getIntent().getExtras().containsKey("refresh")) {
                returnActivityRefresh();
            } else if (getIntent().getExtras().containsKey("bluetooth_conect")) {

                Bundle bundle =  getIntent().getExtras().getBundle("bluetooth_conect");
                id_board = bundle.getInt("id_board");
                getListBoard();
                boardSend = createBoardSend(id_board);

                if(verifyConection()) {
                    boardConected = getBoardConected(id_board_conected);
                    if(boardConected.getId() == boardSend.getId()) { // igual precisa desconectar, esta conectada
                        desconectarBluetooth();
                        // Toast.makeText(activity, "Desconectando board"+ boardConected.getBluetoothName(), Toast.LENGTH_LONG).show();
                    } else {
                        getFinalize();
                        // conectarBluetooth();
                        // Toast.makeText(activity, "Conectando board"+ boardSend.getBluetoothName(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    mConectarBluetooth();
                }
                returnActivityOn();
            }else {
                returnActivityOff();
            }

        }

        /*
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(boardSend.getConectedBluetooth() == 1) {

            Toast.makeText(activity, "Desconectando!", Toast.LENGTH_SHORT).show();
            Log.d("CONX", "Desconectando...");

            BluetoothDevice device = btAdapter.getRemoteDevice(boardConected.getMacAddress());

            try {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.d("CONX", "... CATCH btSocket createRfcommSocketToServiceRecord to Remote...");
            }

            // Discovery is resource intensive. Make sure it isn't going on when you attempt to connect and pass your message.
            btAdapter.cancelDiscovery();

            // Establish the connection. This will block until it connects.
            Log.d("CONX", "...Desconnecting to Remote...");
            try {
                btSocket.close();
            }catch (Exception e) {
                e.getMessage();
            } finally {
                Log.d("CONX", "...Finally...");
                Toast.makeText(activity, "Desconectado", Toast.LENGTH_SHORT).show();
                BoardDAO boardDAO = new BoardDAO(activity);
                boardDAO.conexBluetoothFromBoard(String.valueOf(boardSend.getId()), 0);
                statusSocket = false;
            }

        } else if(boardSend.getMacAddress().isEmpty()) {
            Toast.makeText(activity, "Não é possivel conectar sem mac address", Toast.LENGTH_SHORT).show();
        } else {
            BluetoothDevice device = btAdapter.getRemoteDevice(boardSend.getMacAddress());
            try {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                Log.d("CONX", "... TRY btSocket createRfcommSocketToServiceRecord to Remote...");
            } catch (IOException e) {
                Log.d("CONX", "... CATCH btSocket createRfcommSocketToServiceRecord to Remote...");
                errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
            }

            // Discovery is resource intensive. Make sure it isn't going on when you attempt to connect and pass your message.
            btAdapter.cancelDiscovery();

            // Establish the connection. This will block until it connects.
            Log.d("CONX", "...Connecting to Remote...");
            try {
                btSocket.connect();
                BoardDAO boardDAO = new BoardDAO(activity);
                boardDAO.conexBluetoothFromBoard(String.valueOf(boardSend.getId()), 1);
                statusSocket = true;
            } catch (IOException e) {
                try {
                    btSocket.close();
                    Toast.makeText(activity, "Bluetooth remoto possivelemte desconectado", Toast.LENGTH_LONG).show();
                    statusSocket = false;
                } catch (IOException e2) {
                    errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                }
            }

            // Create a data stream so we can talk to server.
            // Establish the connection. This will block until it connects.
            Log.d("CONX", "...outStream to Remote...");
            try {
                outStream = btSocket.getOutputStream();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
            }
        }
        */

    }

    private Board getBoardConected(int id_board) {

        Board board = null;

        for (Board tBoard: boards) {
            if(tBoard.getId() == id_board) {
                board = tBoard;
            }
        }

        return board;

    }

    private Boolean verifyConection() {

        boolean res = false;
        for (Board tBoard: boards) {
            if(tBoard.getConectedBluetooth() == 1) {
                id_board_conected = (int) tBoard.getId();
                res = true;
            }
        }

        return res;

    }

    private void getListBoard() {

        BoardDAO boardDAO = new BoardDAO(this);
        boards = boardDAO.listaTodos();

    }

    private Board createBoardSend(int id_board) {

        Board tBoardSend = null;
        for (Board tBoard: boards) {
            if((int) tBoard.getId() == id_board) {
                tBoardSend = tBoard;
            }
        }

        return tBoardSend;

    }

    private void desconectarBluetooth() {

        device = getBluetoothAdapter().getRemoteDevice(boardSend.getMacAddress());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                } catch (IOException e) {
                    Log.d("CONX", "... CATCH btSocket createRfcommSocketToServiceRecord to Remote...");
                }

                // Discovery is resource intensive. Make sure it isn't going on when you attempt to connect and pass your message.
                bluetoothAdapter.cancelDiscovery();

                // Establish the connection. This will block until it connects.
                Log.d("CONX", "...Desconnecting to Remote...");
                try {
                    btSocket.close();
                    Log.d("CONX", "...Finally...");
                    Toast.makeText(activity, "Desconectado", Toast.LENGTH_SHORT).show();
                    BoardDAO boardDAO = new BoardDAO(activity);
                    boardDAO.desconexBluetoothFromBoard((String.valueOf(boardSend.getId())));
                    statusSocket = false;
                }catch (Exception e) {
                    e.getMessage();
                }
            }
        }).start();

        returnActivityOff();

    }

    private void mConectarBluetooth() {

        device = getBluetoothAdapter().getRemoteDevice(boardSend.getMacAddress());

        conectarBluetooth(String.valueOf(boardSend.getId()), boardSend.getMacAddress());

    }

    public void returnActivityOff() {

        Bundle pkg = new Bundle();
        pkg.putString("response", "not");

        Intent returnIntent = new Intent();
        returnIntent.putExtra("bluetooth_conection", pkg);
        setResult(this.RESULT_OK,returnIntent);
        finish();

    }

    public void returnActivityRefresh() {

        Intent returnIntent = new Intent();
        setResult(this.RESULT_OK,returnIntent);
        finish();

    }

    public void getFinalize(){
        finish();
    }




}
