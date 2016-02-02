package com.example.cindywang.rewardfitbit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cindywang.rewardfitbit.HomeFragment.OnFragmentInteractionListener;

import org.json.JSONArray;

import java.util.HashMap;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private HashMap<Integer, PlaceholderFragment> mPlaceHolderFragments;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public static final String PREFS_NAME = "MyPrefsFile";
    private ProgressDialog pDialog;
    // URL to get contacts JSON
    private static String url = "http://mathfeud.org/accounts/profile/json";
    // JSON Node names
    private static final String TAG_HEARTDATA = "activities-heart";
    private static final String TAG_VALUE = "value";
    private static final String TAG_HEART_RATE_ZONES = "heartRateZones";
    private  static final String TAG_NAME = "name";
    private static final String TAG_MINUTES = "minutes";

    private static final String FAT_BURN = "Fat Burn";
    private static  final String CARDIO = "Cardio";
    private static final String PEAK = "Peak";

    private static boolean isRunning = false;

    JSONArray heart_data = null;

    HashMap<String, String> mHeartRateMap;

    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mPlaceHolderFragments = new HashMap<>();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        mHeartRateMap = new HashMap<>();

        //new getURLdata(findViewById(android.R.id.content)).execute();

    }

    @Override
    public void updateHeartData() {

    }



    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position){
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance("arg1", "arg2"))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ProfileFragment.newInstance())
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, RewardFragment.newInstance())
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
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

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                //mParam1 = getArguments().getString(ARG_PARAM1);
                //mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //View rootView = new View();
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    TextView curText = (TextView) rootView.findViewById(R.id.section_label);
                    curText.setText(getArguments().getInt(ARG_SECTION_NUMBER) + "");

                    //new getURLdata(rootView).execute();
                    return rootView;
                case 2:
                    View rootView2 = inflater.inflate(R.layout.fragment_profile, container, false);

                    return rootView2;
                    //break;
                case 3:
                    View rootView3 = inflater.inflate(R.layout.fragment_reward, container, false);

                    return rootView3;
                    //break;
                default:
                    View rootViewd = inflater.inflate(R.layout.fragment_main, container, false);
                    curText = (TextView) rootViewd.findViewById(R.id.section_label);
                    curText.setText(getArguments().getInt(ARG_SECTION_NUMBER) + "");
                    return rootViewd;
                    //break;
            }
            //return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


}
