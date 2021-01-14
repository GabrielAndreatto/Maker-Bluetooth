package br.com.example.andreatto.tccmakerbluetooth;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class FragmentBase extends Fragment {
    private MainActivity activityBase;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            activityBase = (MainActivity) context;
        }
    }

    protected MainActivity getActivityBase() {
        return activityBase;
    }

}
