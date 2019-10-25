package br.com.example.andreatto.tccmakerbluetooth.views.listas.bluetoothBonded;

import android.bluetooth.BluetoothDevice;

import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;

public class RecyclerBluetoothBondedListAdapter extends RecyclerView.Adapter<ViewHolderBluetoothBondedList> {

    private List<BluetoothDevice> mBluetoothDeviceList;
    private BluetoothDevice mBluetoothDevice;

    public RecyclerBluetoothBondedListAdapter(List<BluetoothDevice> bluetoothDevices) {
        this.mBluetoothDeviceList = bluetoothDevices;
    }

    @NonNull
    @Override
    public ViewHolderBluetoothBondedList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_bluetooth_bonded, viewGroup, false);
        return new ViewHolderBluetoothBondedList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBluetoothBondedList viewHolderBluetoothBondedList, int position) {
        mBluetoothDevice = mBluetoothDeviceList.get(position);

        viewHolderBluetoothBondedList.nome.setText(mBluetoothDevice.getName());
        viewHolderBluetoothBondedList.numero_mac.setText(mBluetoothDevice.getAddress());
    }


    @Override
    public int getItemCount() {
        return mBluetoothDeviceList.size();
    }

}
