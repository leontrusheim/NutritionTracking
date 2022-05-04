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

    public static String CAL = "calories";
    public static String FAT = "fat";
    public static String PROTEIN = "protein";
    public static String CARB = "carbs";
    public String[] types = {CAL, PROTEIN, FAT, CARB};

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
            else {tv.setText(type + " — " + val + " kcals");}
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
        int proteins = sharedPrefs.getInt(PROTEIN, 50);
        int carbs = sharedPrefs.getInt(CARB, 30);
        int fats = sharedPrefs.getInt(FAT, 20);
        vals = new int[]{cals, proteins, fats, carbs};
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

        seekbars = new SeekBar[]{calSeek, proteinSeek, fatSeek, carbSeek};
        textViews = new TextView[]{calText, proteinText, fatText, carbText};
    }

    public void setOtherBars(){
        if (vals[2] >= 100 - vals[1]){
            int newFat = 100 - vals[1];
            seekbars[2].setProgress(newFat);
            editor.putInt(FAT, newFat);
            editor.commit();
        }
        if (vals[3] != 100 - vals[2] - vals[1]){
            int newCarb = 100 - vals[2] - vals[1];
            seekbars[3].setProgress(newCarb);
            editor.putInt(CARB, newCarb);
            editor.commit();
        }
    }

}