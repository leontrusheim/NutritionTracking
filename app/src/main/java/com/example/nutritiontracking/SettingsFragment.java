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

    public String CAL = "calories";
    public String FAT = "fats";
    public String PROTEIN = "proteins";
    public String CARB = "carbs";
    public String[] types = {CAL, CARB, FAT, PROTEIN};

    public Activity invokerActivity;
    public SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    public SeekBar[] seekbars;
    public TextView[] textViews;
    public int[] vals;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invokerActivity = getActivity();
        sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        getPrefs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        for (int i = 0; i < 4; i++){
            setSeekBars(v);
            SeekBar curr = seekbars[i];
            String type = types[i];
            TextView tv = textViews[i];
            int val = vals[i];
            if (type != CAL) {
                tv.setText(type + " - " + val + "%");
            }
            else {tv.setText(type + " - " + val);}
            curr.setProgress(val);
            curr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    getPrefs();
                    int val = curr.getProgress();
                    if (type != CAL) {
                        tv.setText(type + " - " + val + "%");
                    }
                    else {tv.setText(type + " - " + val);}
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int val = curr.getProgress();
                    editor.putInt(type, val);
                    editor.commit();
                    setSeekBars(v);
                }
            });
        }

        return v;
    }

    public void getPrefs(){
        int cals = sharedPrefs.getInt(CAL, 2000);
        int carbs = sharedPrefs.getInt(CARB, 30);
        int fats = sharedPrefs.getInt(FAT, 20);
        int proteins = sharedPrefs.getInt(PROTEIN, 50);
        vals = new int[]{cals, carbs, fats, proteins};
    }

    public void setSeekBars(View v){
        SeekBar calSeek = v.findViewById(R.id.seek_calories);
        SeekBar carbSeek = v.findViewById(R.id.seek_carbs);
        SeekBar fatSeek = v.findViewById(R.id.seek_fat);
        fatSeek.setMax(100-carbSeek.getProgress());
        SeekBar proteinSeek = v.findViewById(R.id.seek_protein);
        proteinSeek.setProgress(100-carbSeek.getProgress()-fatSeek.getProgress());

        TextView calText = v.findViewById(R.id.calText);
        TextView carbText = v.findViewById(R.id.carbsText);
        TextView fatText = v.findViewById(R.id.fatText);
        TextView proteinText = v.findViewById(R.id.proteinText);
        seekbars = new SeekBar[]{calSeek, carbSeek, fatSeek, proteinSeek};
        textViews = new TextView[]{calText, carbText, fatText, proteinText};
    }
}