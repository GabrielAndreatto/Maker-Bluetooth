package br.com.example.andreatto.tccmakerbluetooth.views.listas.board;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.example.andreatto.tccmakerbluetooth.R;

public class ViewHolderBluetoothBonded extends RecyclerView.ViewHolder{

    public TextView nome;
    public TextView descr;
    public TextView bluetoothName;
    public TextView mac_address;
    public TextView rede;
    public TextView ip;
    public ImageView imgBoard;

    public ViewHolderBluetoothBonded(@NonNull View itemView) {
        super(itemView);

        this.nome = (TextView) itemView.findViewById(R.id.textView_nome);
        this.bluetoothName = (TextView) itemView.findViewById(R.id.textView_nome_bluetooth);
        this.descr = (TextView) itemView.findViewById(R.id.textView_descricao);
        this.mac_address= (TextView) itemView.findViewById(R.id.textView_mac_address);
        this.rede = (TextView) itemView.findViewById(R.id.textView_rede);
        this.ip = (TextView) itemView.findViewById(R.id.textView_ip);
        this.imgBoard = (ImageView) itemView.findViewById(R.id.img_board);
    }
}
