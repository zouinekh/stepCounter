package com.example.stepcounterapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CounterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CounterFragment extends Fragment   {

    private SensorManager sensorManager = null;
    private Sensor stepSensor;
    private int totalSteps=0;
    private int previewsTotalSteps =0;
    private TextView steps;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView counterTextView;
    View rootView;

    public CounterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CounterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CounterFragment newInstance(String param1, String param2) {
        CounterFragment fragment = new CounterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_counter, container, false);
        steps = (TextView) rootView.findViewById(R.id.steps);
        resetSteps();
        loadData();
        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        return rootView;

    }
    private SensorEventListener listener = new SensorEventListener() {
        private static final float ACCELERATION_THRESHOLD = 10f;
        private float previousAcceleration = 0f;

        @Override
        public void onSensorChanged(SensorEvent event) {
            float xValue = event.values[0];

            if (isStepDetected(xValue)) {
                totalSteps++;
                int currentSteps = totalSteps - previewsTotalSteps;
                steps.setText(String.valueOf(currentSteps));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        private boolean isStepDetected(float xValue) {
            float acceleration = Math.abs(xValue - previousAcceleration);
            previousAcceleration = xValue;

            return acceleration > ACCELERATION_THRESHOLD;
        }
    };




    private void resetSteps(){
        steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(),"long press to reset steps ",Toast.LENGTH_SHORT).show();
            }
        });

        steps.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                previewsTotalSteps =totalSteps;
                steps.setText("0");
                saveData();
                return  true;
            }
        });

    }

    private void saveData(){
        SharedPreferences sharedPreferences=requireContext().getSharedPreferences("myPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key1",String.valueOf(previewsTotalSteps));
        editor.apply();
    }
    private void loadData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String savedNumber = sharedPreferences.getString("key1", "0");
        previewsTotalSteps = Integer.parseInt(savedNumber);
    }


}