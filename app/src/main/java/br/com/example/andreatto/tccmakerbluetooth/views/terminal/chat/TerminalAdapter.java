package br.com.example.andreatto.tccmakerbluetooth.views.terminal.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;

public class TerminalAdapter extends BaseAdapter {

    private Context context;

    private List<TerminalMsg> mensagens;

    public TerminalAdapter(Context context, List<TerminalMsg> mensagens) {
        this.context = context;
        this.mensagens = mensagens;
    }

    public void addMensagem(TerminalMsg terminalMsg) {
        mensagens.add(terminalMsg);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mensagens.size();
    }

    @Override
    public Object getItem(int position) {
        return mensagens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        TerminalMsg terminalMsg = mensagens.get(position);

        // Criando uma instância de View a partir de um arquivo de layout.
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(terminalMsg.getCode() == 0) {
            view = inflater.inflate(R.layout.terminal_card_user, null);
        } else {
            view = inflater.inflate(R.layout.terminal_card_board, null);
        }

        // Texto do item a ser apresentado na posição atual da ListView.
        TextView texto = (TextView) view.findViewById(R.id.textView_texto); texto.setText(terminalMsg.getMensagem());
        return view;
    }
}
