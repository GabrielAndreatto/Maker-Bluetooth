package br.com.example.andreatto.tccmakerbluetooth.views.listas.actuators.adapterToggle;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.example.andreatto.tccmakerbluetooth.R;

public class ViewHolderActuatorToggleTab extends RecyclerView.ViewHolder{

    public TextView name;
    public TextView command;

    public ViewHolderActuatorToggleTab(@NonNull View itemView) {
        super(itemView);

        this.name = (TextView) itemView.findViewById(R.id.textView_name);
        this.command = (TextView) itemView.findViewById(R.id.textView_command);
    }
}
