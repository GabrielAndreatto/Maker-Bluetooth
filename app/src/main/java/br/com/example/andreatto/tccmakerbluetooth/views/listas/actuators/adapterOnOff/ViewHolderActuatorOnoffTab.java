package br.com.example.andreatto.tccmakerbluetooth.views.listas.actuators.adapterOnOff;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.example.andreatto.tccmakerbluetooth.R;

public class ViewHolderActuatorOnoffTab extends RecyclerView.ViewHolder{

    public TextView nameOnoff;
    public TextView commandOn;
    public TextView commandOff;

    public ViewHolderActuatorOnoffTab(@NonNull View itemView) {
        super(itemView);

        this.nameOnoff = (TextView) itemView.findViewById(R.id.textView_nome_onoff);
        this.commandOn = (TextView) itemView.findViewById(R.id.textView_command_on);
        this.commandOff = (TextView) itemView.findViewById(R.id.textView_command_off);
    }
}
