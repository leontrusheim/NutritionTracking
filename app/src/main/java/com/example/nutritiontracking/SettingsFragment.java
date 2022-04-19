package com.example.nutritiontracking;

import android.app.Activity;
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

    public Activity invokerActivity;
    public SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    // TODO: Rename and change types of parameters
    public int cals;
    public int carbs;
    private int fats;
    private int proteins;

    public SeekBar calSeek;
    private SeekBar carbSeek;
    private SeekBar fatSeek;
    private SeekBar proteinSeek;

    TextView calText;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invokerActivity = getActivity();
        sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        cals = sharedPrefs.getInt("calories", 499);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        calSeek = v.findViewById(R.id.seek_calories);
        calText = v.findViewById(R.id.calText);
        calText.setText("calories - " + cals);
        System.out.println("CALS INIT: " + cals);
        calSeek.setProgress(cals);
        calSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                cals = calSeek.getProgress();
                calText.setText("calories - " + cals);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt("calories", cals);
                editor.commit();
            }
        });
        return v;
    }
}