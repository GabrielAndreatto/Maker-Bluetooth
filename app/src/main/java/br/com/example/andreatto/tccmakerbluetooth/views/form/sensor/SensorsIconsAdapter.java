package br.com.example.andreatto.tccmakerbluetooth.views.form.sensor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.dao.SensorDAO;
import br.com.example.andreatto.tccmakerbluetooth.modelo.IconsApp;
import br.com.example.andreatto.tccmakerbluetooth.modelo.Sensor;

public class SensorsIconsAdapter extends BaseAdapter {

    private Context context;
    private final List<IconsApp> iconsSensors;
    private SensorDAO sensorDAO;
    private Sensor sensor;

    public SensorsIconsAdapter(Context context, List<IconsApp> sensors) {
        this.context = context;
        this.iconsSensors = sensors;
    }

    @Override
    public int getCount() {
        return iconsSensors.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if(iconsSensors != null) {

            gridView = inflater.inflate(R.layout.card_sensor_icons, null);

            TextView nome = (TextView) gridView.findViewById(R.id.grid_item_name);
            nome.setText(iconsSensors.get(position).getName());

            String uri = "@drawable/" + iconsSensors.get(position).getIcon();
            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
            ImageView image = (ImageView) gridView.findViewById(R.id.grid_item_image);
            Drawable res = context.getResources().getDrawable(imageResource);
            image.setImageDrawable(res);

        } else {
            gridView = (View) convertView;
        }
        return gridView;
    }
}
