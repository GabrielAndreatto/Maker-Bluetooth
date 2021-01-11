package br.com.example.andreatto.tccmakerbluetooth.views.listas.boards;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.jobs.JobServiceBluetoothConn;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Board;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceBluetooth;
import br.com.example.andreatto.tccmakerbluetooth.services.bluetooth.ServiceConnectionBluetoothBind;
import br.com.example.andreatto.tccmakerbluetooth.views.form.board.BoardFormActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.bluetoothBonded.BluetoothBondedListActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.boards.api.DeleteBoardActivity;

public class RecyclerBoardListAdapter extends RecyclerView.Adapter<ViewHolderBluetoothBonded> {
    private static final String LOG_PAGE = "RecyclerBoardAdapter";
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
        initServiceBluetooth();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initServiceBluetooth() {
        try {
            JobScheduler jobScheduler =
                    (JobScheduler) activity.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(new JobInfo.Builder(1,
                    new ComponentName(activity, JobServiceBluetoothConn.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    // .setExtras(new PersistableBundle())
                    .build());
        } catch (Exception e) {
            Log.e(LOG_PAGE, "click listener connect bluetooth try/catch: ");
        } finally {
        }

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

        final Board boardT = boards.get(position);

        //viewHolderBluetoothBonded.nome.setText(board.getNome() +" -> "+ board.getMacAddress().length());
        viewHolderBluetoothBonded.nome.setText(boardT.getNome());
        viewHolderBluetoothBonded.descr.setText(boardT.getDescricao());
        viewHolderBluetoothBonded.bluetoothName.setText(boardT.getBluetoothName());
        viewHolderBluetoothBonded.mac_address.setText(boardT.getMacAddress());
        viewHolderBluetoothBonded.rede.setText(boardT.getRede());
        viewHolderBluetoothBonded.ip.setText(boardT.getIp());

        if (boardT.getConectedBluetooth() == 1) { // true
            viewHolderBluetoothBonded.itemView.findViewById(R.id.cardview_id).setBackgroundColor(viewHolderBluetoothBonded.itemView.getResources().getColor(R.color.board_conected));
            viewHolderBluetoothBonded.imgBoard.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        } else {
            viewHolderBluetoothBonded.imgBoard.setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
            //viewHolderBluetoothBonded.itemView.findViewById(R.id.cardview_id).setBackgroundColor(viewHolderBluetoothBonded.itemView.getResources().getColor(R.color.board_desconected));
        }
        imageViewBoard = viewHolderBluetoothBonded.itemView.findViewById(R.id.img_board);
        imageViewBoard.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (boardT.getMacAddress().isEmpty()) {
                    Toast.makeText(activity, "click listener connect bluetooth is empty", Toast.LENGTH_LONG).show();
                } else {
                    Log.e(LOG_PAGE, "click listener connect bluetooth: " + boardT.getMacAddress());
                    //serviceConnection.getServiceBluetooth().conectarBluetooth(boardT.getMacAddress());

                    Bundle extras = new Bundle();
                    extras.putString("mac_address", boardT.getMacAddress());

                    try {
                        JobScheduler jobScheduler =
                                (JobScheduler) activity.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                        jobScheduler.schedule(new JobInfo.Builder(1,
                                new ComponentName(activity, JobServiceBluetoothConn.class))
                                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                // .setExtras(new PersistableBundle())
                                .build());
                    } catch (Exception e) {
                        Log.e(LOG_PAGE, "click listener connect bluetooth try/catch: " + boardT.getMacAddress());
                    } finally {
                        serviceConnection.getServiceBluetooth().conectarBluetooth(boardT.getMacAddress());
                    }

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
                pkg.putString("id_board", String.valueOf(boardT.getId()));
                Intent i = new Intent(new Intent(activity, BluetoothBondedListActivity.class));
                i.putExtras(pkg);
                activity.startActivityForResult(i, 202);
            }
        });

        imageViewWifi = viewHolderBluetoothBonded.itemView.findViewById(R.id.imageView_wifi);
        imageViewWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext().getApplicationContext(), String.format("Conectar Wifi %d", position), Toast.LENGTH_SHORT).show();
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
                        pkg.putInt("board_id", (int) boardT.getId());
                        pkg.putString("board_nome", boardT.getNome());
                        pkg.putString("board_descr", boardT.getDescricao());
                        pkg.putString("board_bluetooth_name", boardT.getBluetoothName());
                        pkg.putString("board_mac_address", boardT.getMacAddress());
                        pkg.putString("board_rede", boardT.getRede());
                        pkg.putString("board_ip", boardT.getIp());

                        Intent i = new Intent(v.getContext().getApplicationContext(), BoardFormActivity.class);
                        i.putExtras(pkg);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

                                try {
                                    // chamar activity para deletar atuador
                                    Bundle pkg = new Bundle();
                                    pkg.putString("code", "delete");
                                    pkg.putInt("board_id", (int) boardT.getId());

                                    Intent i = new Intent(v.getContext().getApplicationContext(), DeleteBoardActivity.class);
                                    i.putExtras(pkg);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    v.getContext().getApplicationContext().startActivity(i);

                                    Log.e("btnDelete", "onClick Remover Board: " + boardT.getId());

                                } catch (Exception e) {
                                    e.getMessage();
                                    Log.e("REMOVE", "ERROR Board: ");
                                } finally {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, boards.size());
                                        }
                                    });

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showMessageSnack(viewGroup, boardT.getNome());
                                        }
                                    });

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            alertDialog.cancel();
                                        }
                                    });
                                }
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

        TextView snackActionView = snackView.findViewById(R.id.snackbar_action);
        snackActionView.setTextColor(v.getResources().getColor(android.R.color.white, null));

        snackbar.show();
    }

    @Override
    public int getItemCount() {
        return boards.size();
    }

}
