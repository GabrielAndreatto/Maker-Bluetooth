package br.com.example.andreatto.tccmakerbluetooth.views.listas.buttons.btn_one_cmd;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.example.andreatto.tccmakerbluetooth.R;

public class ViewHolderBtnOne extends RecyclerView.ViewHolder{

    public TextView nome;
    public TextView comando;
    public ImageView imgBtnClick;

    public ViewHolderBtnOne(@NonNull View itemView) {
        super(itemView);

        this.nome = (TextView) itemView.findViewById(R.id.textView_nome);
        this.comando = (TextView) itemView.findViewById(R.id.textView_nome);
        this.imgBtnClick = (ImageView) itemView.findViewById(R.id.img_board);
    }
}
