package br.com.example.andreatto.tccmakerbluetooth.views.form.sensor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.IconsAppDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.IconsApp;
import br.com.example.andreatto.tccmakerbluetooth.utils.classes.Print;

public class IconsSensorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private GridView gridView;
    private Print print = new Print();

    private IconsApp iconsApp;
    private IconsAppDAO iconsAppDAO;
    private List<IconsApp> iconsAppList;

    private Bundle pacote;
    private Bundle pkgRet;
    private Intent returnIntent;
    private int iconBaseId;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icons_sensor);

        iconsApp = new IconsApp();
        iconsAppDAO = new IconsAppDAO(this);

        pacote = new Bundle();
        pkgRet = new Bundle();
        returnIntent = new Intent();

        print.logE("onCreate", "onCreate onCreate onCreate");

        pacote = getIntent().getExtras();
        if(!pacote.isEmpty() || getIntent().getExtras() != null) {

            try{
                iconBaseId = Integer.parseInt(pacote.getString("icon_base"));

                print.logE("URIT", String.valueOf(iconBaseId));

            } catch(Exception e) {
                print.logE("ERRORRRR", e.getMessage());
            }

        }

        toolbar = findViewById(R.id.toolbar_sensor);
        toolbar.setTitle("Icones");
        setSupportActionBar(toolbar);

        initialListIconSensor();
        iconsApp = iconsAppList.get(iconBaseId);

        gridView = findViewById(R.id.simpleGridView);
        gridView.setAdapter(new SensorsIconsAdapter(getApplicationContext(), iconsAppList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                iconBaseId = position;
                iconsApp = iconsAppList.get(iconBaseId);
                returnActivityOn();
            }
        });

    }

    private void initialListIconSensor() {
        iconsAppList = iconsAppDAO.all();
    }

    private IconsApp getIconSensorWithId(int _id) {
        IconsApp IconsAppT = new IconsApp();
        for (IconsApp tmpIconsApp: iconsAppList) {
            if(tmpIconsApp.getId() == _id) {
                IconsAppT = tmpIconsApp;
            }
        }
        return IconsAppT;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void returnActivityOn() {
        print.logE("returnActiv", "return Activity Onnnnn ");
        print.logE("rposition", "rposition: "+ iconBaseId);
        print.logE("ICON", "id: "+iconsApp.getId());
        print.logE("ICON", "name: "+iconsApp.getName());
        print.logE("ICON", "Icon: "+iconsApp.getIcon());

        pkgRet.putString("action", "selected");
        pkgRet.putInt("icon",iconBaseId);
        returnIntent.putExtras(pkgRet);
        setResult(343, returnIntent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void returnOnBackPressed() {
        print.logE("ICON", "id: "+iconsApp.getId());
        print.logE("ICON", "name: "+iconsApp.getName());
        print.logE("ICON", "Icon: "+iconsApp.getIcon());
        print.logE("BackPressed", "BackPressed BackPressed");
        print.logE("rposition", "rposition: "+ iconBaseId);

        pkgRet.putString("action", "selected");
        pkgRet.putInt("icon", iconBaseId);
        returnIntent.putExtras(pkgRet);
        setResult(343, returnIntent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        returnOnBackPressed();
        //super.onBackPressed();
    }
}