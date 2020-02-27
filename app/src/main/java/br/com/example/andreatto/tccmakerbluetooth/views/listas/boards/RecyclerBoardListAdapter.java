package br.com.example.andreatto.tccmakerbluetooth.views.listas.boards;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.BoardDAO;
import br.com.example.andreatto.tccmakerbluetooth.util.bluetooth.activitys.AppCompatActivityBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.views.form.board.BoardFormActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.bluetoothBonded.BluetoothBondedListActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.boards.list.BluetoothListActivityForActivate;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Board;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceConnectionBluetoothBind;

public class RecyclerBoardListAdapter extends RecyclerView.Adapter<ViewHolderBluetoothBonded> {

    private final String TAG = "BLUETOOTH";
    private List<Board> boards;
    private ImageView imageViewBoard;
    private ImageView imageViewBluetooth;
    private ImageView imageViewWifi;
    private ImageView imageViewEditar;
    private ImageView imageViewDeletar;

    Activity activity;

    // SERVICE
    private Intent intentService;
    private ServiceConnectionBluetoothBind serviceConnection;

    public RecyclerBoardListAdapter(List<Board> boards, Activity activity) {
        this.boards = boards;
        this.activity = activity;

        intentService = new Intent(activity, ServiceBluetooth.class);
        serviceConnection = new ServiceConnectionBluetoothBind();
        getActivity().getApplicationContext().bindService(intentService, serviceConnection, 0);
    }

    private Activity getActivity() {
        return this.activity;
    }

    @NonNull
    @Override
    public ViewHolderBluetoothBonded onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_board, viewGroup, false);
        return new ViewHolderBluetoothBonded(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderBluetoothBonded viewHolderBluetoothBonded, final int position) {

        final Board board = boards.get(position);

        //viewHolderBluetoothBonded.nome.setText(board.getNome() +" -> "+ board.getMacAddress().length());
        viewHolderBluetoothBonded.nome.setText(board.getNome());
        viewHolderBluetoothBonded.descr.setText(board.getDescricao());
        viewHolderBluetoothBonded.bluetoothName.setText(board.getBluetoothName());
        viewHolderBluetoothBonded.mac_address.setText(board.getMacAddress());
        viewHolderBluetoothBonded.rede.setText(board.getRede());
        viewHolderBluetoothBonded.ip.setText(board.getIp());

        if(board.getConectedBluetooth() == 1) { // true
            viewHolderBluetoothBonded.itemView.findViewById(R.id.cardview_id).setBackgroundColor(viewHolderBluetoothBonded.itemView.getResources().getColor(R.color.board_conected));
            viewHolderBluetoothBonded.imgBoard.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }else {
            viewHolderBluetoothBonded.imgBoard.setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
            //viewHolderBluetoothBonded.itemView.findViewById(R.id.cardview_id).setBackgroundColor(viewHolderBluetoothBonded.itemView.getResources().getColor(R.color.board_desconected));

        }
        imageViewBoard = viewHolderBluetoothBonded.itemView.findViewById(R.id.img_board);
        imageViewBoard.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (board.getMacAddress().isEmpty()) {
                    Toast.makeText(activity, "Adicionar um mac address", Toast.LENGTH_LONG).show();
                } else {
                    serviceConnection.getServiceBluetooth().conectarBluetooth(board.getMacAddress());
                }

            }
        });

        imageViewBluetooth = viewHolderBluetoothBonded.itemView.findViewById(R.id.imageView_bluetooth);
        imageViewBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(v.getContext().getApplicationContext(), "position"+board.getId(), Toast.LENGTH_SHORT).show();
                Bundle pkg = new Bundle();
                pkg.putString("code", "bluetooth-edit");
                pkg.putString("id_board", String.valueOf(board.getId()));
                Intent i = new Intent(new Intent(activity, BluetoothBondedListActivity.class));
                i.putExtras(pkg);
                activity.startActivityForResult(i, 202);

            }
        });

        imageViewWifi = viewHolderBluetoothBonded.itemView.findViewById(R.id.imageView_wifi);
        imageViewWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext().getApplicationContext(), String.format("Conectar Wifi%d", position), Toast.LENGTH_SHORT).show();
            }
        });

        imageViewEditar = viewHolderBluetoothBonded.itemView.findViewById(R.id.imageView_editar);
        imageViewEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.post(new Runnable() {
                    @Override
                    public void run() {

                        Bundle pkg = new Bundle();
                        pkg.putString("code", "editar");
                        pkg.putInt("board_id", (int) board.getId());
                        pkg.putString("board_nome", board.getNome());
                        pkg.putString("board_descr", board.getDescricao());
                        pkg.putString("board_bluetooth_name", board.getBluetoothName());
                        pkg.putString("board_mac_address", board.getMacAddress());
                        pkg.putString("board_rede", board.getRede());
                        pkg.putString("board_ip", board.getIp());

                        Intent i = new Intent(v.getContext().getApplicationContext(), BoardFormActivity.class);
                        i.putExtras(pkg);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                        v.getContext().getApplicationContext().startActivity(i);

                    }
                });
            }
        });

        imageViewDeletar = viewHolderBluetoothBonded.itemView.findViewById(R.id.imageView_deletar);
        imageViewDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //showCustomDialog(board, position);

                        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                        final ViewGroup viewGroup = activity.findViewById(android.R.id.content);

                        //then we will inflate the custom alert dialog xml that we created
                        final View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_confirm_board, viewGroup, false);

                        //Now we need an AlertDialog.Builder object
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                        //setting the view of the builder to our custom view that we already inflated
                        builder.setView(dialogView);

                        //finally creating the alert dialog and displaying it
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        dialogView.findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(View v) {

                                BoardDAO boardDAO = new BoardDAO(v.getContext().getApplicationContext());
                                boardDAO.remover(board);

                                try {
                                    Thread.sleep(100);
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, boards.size());
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    Thread.sleep(200);
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showMessageSnack(viewGroup, board.getNome());
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    Intent intent = new Intent(activity, AppCompatActivityBluetooth.class);
                                    intent.putExtra("refresh", "ok");

                                    Thread.sleep(150);
                                    activity.startActivityForResult(intent, 201);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                alertDialog.cancel();
                            }
                        });

                        dialogView.findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.cancel();
                            }

                        });
                    }
                });


            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showMessageSnack(View v, String paramNomeBoard) {
        final Snackbar snackbar = Snackbar.make(v, "A Board " + paramNomeBoard + " foi removida com sucesso", Snackbar.LENGTH_LONG);

        View snackView = snackbar.getView();
        snackView.setBackgroundColor(v.getResources().getColor(R.color.secondaryTextColor));

        TextView snackActionView  = snackView.findViewById(android.support.design.R.id.snackbar_action);
        snackActionView.setTextColor(v.getResources().getColor(android.R.color.white, null));

        snackbar.show();

    }

    @Override
    public int getItemCount() {
        return boards.size();
    }

}
