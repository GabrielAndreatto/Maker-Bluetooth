package br.com.example.andreatto.tccmakerbluetooth.views.listas.bluetoothBonded;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.example.andreatto.tccmakerbluetooth.R;

public class ViewHolderBluetoothBondedList extends RecyclerView.ViewHolder{

    public TextView nome;
    public TextView numero_mac;

    public ViewHolderBluetoothBondedList(@NonNull View itemView) {
        super(itemView);

        this.nome = (TextView) itemView.findViewById(R.id.textView_Bluetooth_bonded_nome);
        this.numero_mac = (TextView) itemView.findViewById(R.id.textView_Bluetooth_bonded_mac);

    }
}
