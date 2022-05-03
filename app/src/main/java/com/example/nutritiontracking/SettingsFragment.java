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

    public String CAL = "CALORIES";
    public String FAT = "FATS";
    public String PROTEIN = "PROTEINS";
    public  String CARB = "CARBS";
    public String[] types = {CAL, CARB, FAT, PROTEIN};

    public Activity invokerActivity;
    public static SharedPreferences sharedPrefs;
    static SharedPreferences.Editor editor;

    public SeekBar[] seekbars;
    public TextView[] textViews;
    public static int[] vals;

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
                tv.setText(type + " — " + val + "%");
            }
            else {tv.setText(type + " — " + val + " cals");}
            curr.setProgress(val);
            curr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    int val = curr.getProgress();
                    if (type != CAL) {
                        tv.setText(type + " — " + val + "%");
                    }
                    else {tv.setText(type + " — " + val + " cals");}
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int val = curr.getProgress();
                    editor.putInt(type, val);
                    editor.commit();
                    getPrefs();
                    setOtherBars();
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
        SeekBar proteinSeek = v.findViewById(R.id.seek_protein);

        TextView calText = v.findViewById(R.id.calText);
        TextView carbText = v.findViewById(R.id.carbsText);
        TextView fatText = v.findViewById(R.id.fatText);
        TextView proteinText = v.findViewById(R.id.proteinText);

        seekbars = new SeekBar[]{calSeek, carbSeek, fatSeek, proteinSeek};
        textViews = new TextView[]{calText, carbText, fatText, proteinText};
    }

    public void setOtherBars(){
        if (vals[2] >= 100 - vals[1]){
            int newFat = 100 - vals[1];
            seekbars[2].setProgress(newFat);
            editor.putInt(FAT, newFat);
            editor.commit();
        }
        if (vals[3] != 100 - vals[2] - vals[1]){
            int newProtein = 100 - vals[2] - vals[1];
            seekbars[3].setProgress(newProtein);
            editor.putInt(PROTEIN, newProtein);
            editor.commit();
        }
    }
}