package br.com.example.andreatto.tccmakerbluetooth;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class FragmentModel extends Fragment {
    private MainActivity activityMain;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            activityMain = (MainActivity) context;
        }
    }

    protected MainActivity getActivityBase() {
        return activityMain;
    }

}
