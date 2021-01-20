package br.com.example.andreatto.tccmakerbluetooth.views.listas.actuators;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import br.com.example.andreatto.tccmakerbluetooth.R;
import br.com.example.andreatto.tccmakerbluetooth.utils.classes.Print;
import br.com.example.andreatto.tccmakerbluetooth.views.form.actuator.ActuatorFormActivity;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.actuators.frgmnts.TabOnOff;
import br.com.example.andreatto.tccmakerbluetooth.views.listas.actuators.frgmnts.TabToggle;

public class ActuatorActivityTab extends AppCompatActivity {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager mViewPager;

    Activity activity;

    private Toolbar toolbar;
    private Print print = new Print();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actuator_tab);

        activity = this;
        initialUI();
        initialAdapter();
        initialTabBar();
    }

    private void initialAdapter() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), activity);
        // sectionsPagerAdapter.notifyDataSetChanged();
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewpager_tab_actuators);
        mViewPager.setAdapter(sectionsPagerAdapter);
    }

    private void initialTabBar() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_actuators);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    private void initialUI() {
        toolbar = findViewById(R.id.toolbar_actuator);
        toolbar.setTitle("Atuadores");
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialUI();
        initialAdapter();
        initialTabBar();
        // sectionsPagerAdapter.notifyDataSetChanged();
        Log.e("ActuatorActivityTab", "onResume Actuator Activity Tab");
    }

    // Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_sensor, menu);
        return true;
    }

    // Toolbar item menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_menu:
                Bundle pkg = new Bundle();
                pkg.putString("code", "new");
                Intent i = new Intent(this, ActuatorFormActivity.class);
                i.putExtras(pkg);
                startActivityForResult(i, 200);
                break;
            case R.id.about_menu:
                print.toast(this, "Sobre ", true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @SuppressLint("StringFormatInvalid")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        Activity activity;

        public SectionsPagerAdapter(FragmentManager fm, Activity activity) {
            super(fm);
            this.activity = activity;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new TabToggle(activity);
                case 1:
                    return new TabOnOff(activity);
//                case 2:
//                    Tab3 tab3 = new Tab3();
//                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages. Possible some
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0)
            {
                title = "TOGGLE";
            }
            else if (position == 1)
            {
                title = "ON/OFF";
            }

            return title;
        }

    }

}
