package br.com.example.andreatto.tccmakerbluetooth.views.listas.actuators.adapterOnOff;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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
