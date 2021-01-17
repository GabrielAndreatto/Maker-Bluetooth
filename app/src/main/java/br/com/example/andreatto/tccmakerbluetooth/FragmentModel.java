package br.com.example.andreatto.tccmakerbluetooth;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentModel extends Fragment {
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