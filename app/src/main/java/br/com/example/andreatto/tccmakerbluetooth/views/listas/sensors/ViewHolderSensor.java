package br.com.example.andreatto.tccmakerbluetooth.views.listas.sensors;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.example.andreatto.tccmakerbluetooth.R;

public class ViewHolderSensor extends RecyclerView.ViewHolder{

    public TextView name;
    public TextView command;
    public TextView value;
    public TextView used_day;
    public TextView used_hour;

    public ViewHolderSensor(@NonNull View itemView) {
        super(itemView);
        this.name = (TextView) itemView.findViewById(R.id.card_sensor_name);
        this.command = (TextView) itemView.findViewById(R.id.card_sensor_command);
        this.value = (TextView) itemView.findViewById(R.id.card_sensor_value);
        this.used_day = (TextView) itemView.findViewById(R.id.card_sensor_date);
        this.used_hour = (TextView) itemView.findViewById(R.id.card_sensor_hour);
    }
}
