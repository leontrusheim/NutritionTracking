package com.example.nutritiontracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsFragment extends Fragment {


    // TODO: Rename and change types of parameters
    private int cals;
    private int carbs;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cals = getArguments().getInt("calories");
            carbs = getArguments().getInt("carbs");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        final SeekBar calSeek = v.findViewById(R.id.seek_calories);
        calSeek.setProgress((cals / 2000)* 100);
        calSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                SharedPreferences sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                int calVal = calSeek.getProgress() / 100 * 2000;
                editor.putInt("calories", calVal);
                TextView tv = v.findViewById(R.id.calText);
                tv.setText("Calories" + calVal);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return v;
    }
}