package com.example.cindywang.rewardfitbit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FATBURN = "Fat Burn";
    private static final String ARG_CARDIO = "Cardio";

    // TODO: Rename and change types of parameters
    private String fatBurn;
    private String cardio;

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

    JSONArray heart_data = null;

    HashMap<String, String> mHeartRateMap = new HashMap<>();

    private OnFragmentInteractionListener mListener;

    public static final String PREFS_NAME = "MyPrefsFile";

    private static boolean isRunning = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FATBURN, param1);
        args.putString(ARG_CARDIO, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        new getURLdata().execute();
        if (getArguments() != null) {
            fatBurn = getArguments().getString(ARG_FATBURN);
            cardio = getArguments().getString(ARG_CARDIO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button mButton = (Button) rootView.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRunning();
            }
        });
        return rootView;
    }

    void startRunning() {
        Button runningButton = (Button)getView().findViewById(R.id.button);
        if (isRunning) {

            runningButton.setText("START RUNNING!");
        } else {

            runningButton.setText("STOP");
        }
        isRunning = !isRunning;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach((Activity) activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;

        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
        void updateHeartData();
    }



    private class getURLdata extends AsyncTask<Void, Void, Void> {


        public getURLdata(){

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            /*
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show(); */

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    // Getting JSON Array node
                    heart_data = jsonObj.getJSONArray(TAG_HEARTDATA);
                    if (heart_data.length() >= 1){
                        JSONObject c = heart_data.getJSONObject(0);
                        JSONObject value = c.getJSONObject(TAG_VALUE);
                        JSONArray heart_rates = value.getJSONArray(TAG_HEART_RATE_ZONES);

                        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        for (int i = 0; i < heart_rates.length(); i++) {
                            JSONObject c_heart = heart_rates.getJSONObject(i);
                            String name = c_heart.getString(TAG_NAME);
                            int minutes = c_heart.getInt(TAG_MINUTES);
                            mHeartRateMap.put(name, minutes+"");

                            editor.putString(name, minutes+"");
                        }

                        String scoreStr = String.format("%.2f", getScore());
                        mHeartRateMap.put("score",scoreStr );
                        editor.putString("score", scoreStr);

                        editor.commit();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;

        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /**
             * Updating parsed JSON data into ListView
             * */

            TextView fatburn_textview = (TextView)getView().findViewById(R.id.fatburntext);
            TextView cardio_textview = (TextView)getView().findViewById(R.id.cardiotext);
            TextView peak_textview = (TextView)getView().findViewById(R.id.peaktext);
            TextView score_textview = (TextView)getView().findViewById(R.id.pointtext);

            score_textview.setText(mHeartRateMap.get("score" ));

            if (mHeartRateMap.containsKey(FAT_BURN))
                fatburn_textview.setText( String.format("%.2f",
                        getToGo(Integer.parseInt(mHeartRateMap.get(FAT_BURN).trim()),  0.2))
                        + " minutes");
            if (mHeartRateMap.containsKey(CARDIO))
                cardio_textview.setText(String.format("%.2f",
                        getToGo(Integer.parseInt(mHeartRateMap.get(CARDIO).trim()), 0.3))
                        + " minutes");
            if (mHeartRateMap.containsKey(PEAK))
                peak_textview.setText(String.format("%.2f",
                        getToGo(Integer.parseInt(mHeartRateMap.get(PEAK).trim()), 0.4))
                        + " minutes");

        }
    }

    double getScore() {
        int fat_burn_minute = 0;
        int cardio_minute = 0;
        int peak_minute = 0;
        if (mHeartRateMap.containsKey(FAT_BURN))
            fat_burn_minute = Integer.parseInt(mHeartRateMap.get(FAT_BURN).trim());
        if (mHeartRateMap.containsKey(CARDIO))
            cardio_minute = Integer.parseInt(mHeartRateMap.get(CARDIO).trim());
        if (mHeartRateMap.containsKey(PEAK))
            peak_minute = Integer.parseInt(mHeartRateMap.get(PEAK).trim());

        return 20 - fat_burn_minute*0.2 - cardio_minute*0.3 - peak_minute*0.4;
    }

    double getToGo(int minute, double weight) {

        Double score = 0.0;
        if (mHeartRateMap.containsKey("score"))
            score = Double.valueOf(mHeartRateMap.get("score").trim());
        return (score )/weight;
    }
}
