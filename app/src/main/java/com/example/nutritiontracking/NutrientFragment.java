package com.example.nutritiontracking;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class NutrientFragment extends Fragment {
    public Activity containerActivity = null;
    String category;

    public NutrientFragment() {
        // Required empty public constructor
    }
    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nutrient, container, false);
        String info = getArguments().getString("info");
        TextView textView = v.findViewById(R.id.header);
        textView.setText(info);
        String[] nutrients =getArguments().getStringArray("nutrients");
        ArrayAdapter arrayAdapter = new ArrayAdapter (containerActivity, R.layout.nutrient_list_row,
                R.id.nutrient_list_row_item, nutrients) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(R.id.nutrient_list_row_item);
                if (position == 0){
                    category = "calories: ";
                }else if (position == 1){
                    category = "carbs: ";
                }else if (position == 2){
                    category = "fat: ";
                } else {
                    category = "protein: ";
                }
                String text = category + nutrients[position];
                textView.setText(text);
                return view;
            }
        };
        ListView listView = v.findViewById(R.id.nutrient_list);
        listView.setAdapter(arrayAdapter);

        return v;
    }


}