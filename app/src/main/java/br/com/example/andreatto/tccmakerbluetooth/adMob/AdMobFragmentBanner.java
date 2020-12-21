package br.com.example.andreatto.tccmakerbluetooth.adMob;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.com.example.andreatto.tccmakerbluetooth.R;

public class AdMobFragmentBanner extends Fragment {
    // Remove the below line after defining your own ad unit ID.
    private static final String LOG_PAGE = "AdMobFragmentBanner";
    private static final String TOAST_TEXT = "Test ads dMob.";
    private static final String DEVICE_ID = "R9QN803HPDE";
    AdView adView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ad_mob, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intiAdMob(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() == null || getActivity().getApplicationContext() == null) return;
        final Context appContext = getActivity().getApplicationContext();
        // Toasts the test ad message on the screen.
        // Remove this after defining your own ad unit ID.
        Toast.makeText(appContext, TOAST_TEXT, Toast.LENGTH_LONG).show();
    }

    private void intiAdMob(View view) {
        MobileAds.initialize(getActivity(), getString(R.string.adMob_app_id));

        // Load an ad into the AdMob banner view.
        adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(DEVICE_ID)
                //.addTestDevice("TEST_DEVICE_ID")
                //.setRequestAgent("android_studio:ad_template")
                .build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.e(LOG_PAGE, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.e(LOG_PAGE, "onAdFailedToLoad");

                switch (i) {
                    case 0:
                        Log.e(LOG_PAGE, "ERROR_CODE_INTERNAL_ERROR: an invalid response was received from the ad server adMob"); // an invalid response was received from the ad server adMob
                        break;
                    case 1:
                        Log.e(LOG_PAGE, "ERROR_CODE_INVALID_REQUEST: The ad unit ID is incorrect."); // The ad unit ID is incorrect.
                        break;
                    case 2:
                        Log.e(LOG_PAGE, "ERROR_CODE_NETWORK_ERROR There is no network connection at that moment and the ad request fails."); // There is no network connection at that moment and the ad request fails.
                        break;
                    case 3:
                        Log.e(LOG_PAGE, "ERROR_CODE_NO_FILL: The settings for requesting ad from ad server are correct and there is a successful response"); // The settings for requesting ad from ad server are correct and there is a successful response
                        break;
                    case 8:
                        break;
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.e(LOG_PAGE, "onAdOpened");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.e(LOG_PAGE, "onAdClicked");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.e(LOG_PAGE, "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.e(LOG_PAGE, "onAdClosed");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        adView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adView.destroy();
    }
}