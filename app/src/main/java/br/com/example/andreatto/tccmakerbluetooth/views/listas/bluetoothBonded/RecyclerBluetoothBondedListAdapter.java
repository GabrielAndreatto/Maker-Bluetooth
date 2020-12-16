package br.com.example.andreatto.tccmakerbluetooth.views.listas.bluetoothBonded;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.BoardDAO;

public class RecyclerBluetoothBondedListAdapter extends RecyclerView.Adapter<ViewHolderBluetoothBondedList> {

    private static final String LOG_PAGE = "RecyclerBluetoothBondedListAdapter";
    private Activity activity;
    private List<BluetoothDevice> mBluetoothDeviceList;
    private String code, idBoard;

    public RecyclerBluetoothBondedListAdapter(Activity activity, String code, String idBoard, List<BluetoothDevice> mBluetoothDeviceList) {
        this.activity = activity;
        this.code = code;
        this.idBoard = idBoard;
        this.mBluetoothDeviceList = mBluetoothDeviceList;
    }

    @NonNull
    @Override
    public ViewHolderBluetoothBondedList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_bluetooth_bonded, viewGroup, false);
        return new ViewHolderBluetoothBondedList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBluetoothBondedList viewHolderBluetoothBondedList, int position) {
        final BluetoothDevice mBluetoothDeviceT = mBluetoothDeviceList.get(position);

        viewHolderBluetoothBondedList.nome.setText(mBluetoothDeviceT.getName());
        viewHolderBluetoothBondedList.numero_mac.setText(mBluetoothDeviceT.getAddress());
        View viewById = viewHolderBluetoothBondedList.itemView.findViewById(R.id.card_ble_bonded);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(code.equals("bluetooth-none")) {
                    Log.e(LOG_PAGE, "bluetooth-none");
                    Toast.makeText(activity, "Bluetooth name: " + mBluetoothDeviceT.getName() +  " | Mac Address: " + mBluetoothDeviceT.getAddress(), Toast.LENGTH_SHORT).show();
                }
                if(code.equals("bluetooth-macAddress")) {
                    Log.e(LOG_PAGE, "bluetooth-macAddress-adapter");
                    Bundle pkgT = new Bundle();
                    pkgT.putString("code", "bluetooth-macAddress");
                    pkgT.putString("bluetooth-name", mBluetoothDeviceT.getName());
                    pkgT.putString("bluetooth-mac-address", mBluetoothDeviceT.getAddress());
                    Intent returnIntent = new Intent();
                    returnIntent.putExtras(pkgT);
                    activity.setResult(202, returnIntent);
                    activity.finish();
                }
                if(code.equals("bluetooth-edit")) {
                    Log.e(LOG_PAGE, "bluetooth-edit: ");
                    try{
                        BoardDAO boardDAO = new BoardDAO(activity);
                        boardDAO.atualizarBluetoothBoard(idBoard, mBluetoothDeviceT.getAddress(), mBluetoothDeviceT.getName());
                        Log.e(LOG_PAGE, "bluetooth-edit try: ");
                    } catch (Exception e) {
                        Log.e(LOG_PAGE, "bluetooth-edit catch: ");
                        activity.finish();
                    } finally {
                        Log.e(LOG_PAGE, "bluetooth-edit finally: ");
                        Bundle pkgT = new Bundle();
                        pkgT.putString("bluetooth-name", mBluetoothDeviceT.getName());
                        pkgT.putString("bluetooth-mac-address", mBluetoothDeviceT.getAddress());
                        Intent returnIntent = new Intent();
                        returnIntent.putExtras(pkgT);
                        activity.setResult(202,returnIntent);
                        activity.finish();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBluetoothDeviceList.size();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        Log.e(LOG_PAGE, "bluetooth-macAddress-adapter-finalize");
        Bundle pkgT = new Bundle();
        pkgT.putString("code", "bluetooth-macAddress");
        pkgT.putString("bluetooth-name", "");
        pkgT.putString("bluetooth-mac-address", "");
        Intent returnIntent = new Intent();
        returnIntent.putExtras(pkgT);
        activity.setResult(202, returnIntent);
        activity.finish();
    }
}
