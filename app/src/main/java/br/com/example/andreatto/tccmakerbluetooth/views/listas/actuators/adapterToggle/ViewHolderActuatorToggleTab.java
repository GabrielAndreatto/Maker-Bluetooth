package br.com.example.andreatto.tccmakerbluetooth.views.listas.actuators.adapterToggle;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
