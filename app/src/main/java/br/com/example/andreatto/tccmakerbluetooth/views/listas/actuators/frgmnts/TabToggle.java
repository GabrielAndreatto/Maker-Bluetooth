package br.com.example.andreatto.tccmakerbluetooth.views.listas.actuators.frgmnts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.ActuactorDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Actuator;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.actuators.adapterToggle.ActuatorsToggleListAdapterTab;

@SuppressLint("ValidFragment")
public class TabToggle extends Fragment {

    private List<Actuator> actuatorToggleList;
    private List<Actuator> actuatorsToggleConvert;
    private ActuactorDAO actuactorToggleDAO;
    private RecyclerView recyclerToggleView;
    private ActuatorsToggleListAdapterTab actuatorsToggleListAdapterTab;
    private Activity activityToggle;

    @SuppressLint("ValidFragment")
    public TabToggle(Activity activity) {
        this.activityToggle = activity;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TabToggle", "TabToggle onPause: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TabToggle", "TabToggle onResume: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1, container,false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.e("TabToggle", "TabToggle onViewCreated: ");

        actuatorToggleList = new ArrayList<>();
        actuatorsToggleConvert = new ArrayList<>();

        recyclerToggleView = (RecyclerView) view.findViewById(R.id.recyclerview_actuators_toggle);
        recyclerToggleView.setHasFixedSize(true);
        recyclerToggleView.setLayoutManager(new LinearLayoutManager(activityToggle));
        // recyclerToggleView.invalidate();
        // recyclerToggleView.setItemViewCacheSize(Integer.MIN_VALUE);
        // recyclerToggleView.getRecycledViewPool().clear();
        // recyclerToggleView.setItemViewCacheSize(2);

        // get list actuator toggle
        getData();

    }

    private void getData() {
        actuactorToggleDAO = new ActuactorDAO(activityToggle);
        actuatorToggleList = actuactorToggleDAO.allToggle();
        setupData(actuatorToggleList);
    }

    private void setupData(List<Actuator> actuators) {
        actuatorsToggleListAdapterTab = new ActuatorsToggleListAdapterTab(actuators, activityToggle);
        recyclerToggleView.setAdapter(actuatorsToggleListAdapterTab);
    }
}
