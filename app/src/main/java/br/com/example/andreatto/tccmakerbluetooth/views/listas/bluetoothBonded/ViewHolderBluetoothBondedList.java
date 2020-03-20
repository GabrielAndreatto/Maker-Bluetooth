package br.com.example.andreatto.tccmakerbluetooth.views.listas.bluetoothBonded;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
