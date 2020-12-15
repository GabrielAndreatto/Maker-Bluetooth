package br.com.example.andreatto.tccmakerbluetooth.views.listas.actuators.frgmnts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.ActuactorDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Actuator;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.actuators.adapterOnOff.ActuatorsOnoffListAdapterTab;

@SuppressLint("ValidFragment")
public class TabOnOff extends Fragment {

    private List<Actuator> actuatorOnOffList;
    private List<Actuator> actuatorsOnOffConvertList;
    private ActuactorDAO actuactorOnOffDAO;
    private RecyclerView recyclerOnOffView;
    private ActuatorsOnoffListAdapterTab actuatorsOnoffListAdapterTab;
    private Activity activityOnOff;

    @SuppressLint("ValidFragment")
    public TabOnOff(Activity activity) {
        this.activityOnOff = activity;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TabOnOff", "TabOnOff onPause: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TabOnOff", "TabOnOff onResume: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("TabOnOff", "TabOnOff onCreateView: ");
        View view = inflater.inflate(R.layout.actuator_on_off_tab, container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("TabOnOff", "TabOnOff onViewCreated: ");

        recyclerOnOffView = (RecyclerView) view.findViewById(R.id.recyclerview_actuators_onoff);
        recyclerOnOffView.setHasFixedSize(true);
        recyclerOnOffView.setLayoutManager(new LinearLayoutManager(activityOnOff));

        actuatorOnOffList = new ArrayList<>();
        actuatorsOnOffConvertList = new ArrayList<>();

        // get list actuator toggle
        getData();
    }

    private void getData() {
        actuactorOnOffDAO = new ActuactorDAO(activityOnOff);
        actuatorOnOffList = actuactorOnOffDAO.allOnoff();
        setupData(actuatorOnOffList);
    }

    private void setupData(List<Actuator> actuators) {
        actuatorsOnoffListAdapterTab = new ActuatorsOnoffListAdapterTab(actuators, activityOnOff);
        recyclerOnOffView.setAdapter(actuatorsOnoffListAdapterTab);
    }

}
