package com.example.nutritiontracking;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class FoodSearchFragment extends Fragment {
    public Activity containerActivity = null;

    public FoodSearchFragment() {
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
        View v = inflater.inflate(R.layout.fragment_food_search, container, false);
        String foodName = getArguments().getString("keyword");
        EditText text = v.findViewById(R.id.edit_food);
        text.setText(foodName);
        ArrayList<String[]> items = (ArrayList<String[]>) getArguments().
                getSerializable("items");
        HashMap<String[],String[]> nutrients = (HashMap<String[], String[]>) getArguments().
                getSerializable("nutrients");
        ArrayAdapter arrayAdapter = new ArrayAdapter (containerActivity, R.layout.food_list_row,
                R.id.food_list_row_item, items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(R.id.food_list_row_item);
                String[] info = items.get(position);
                String text = "Item Name: " + info[0] + " Brand Name: " + info[1];
                textView.setText(text);
                textView.setTag(nutrients.get(info));
                return view;
            }
        };
        ListView listView = v.findViewById(R.id.food_list);
        listView.setAdapter(arrayAdapter);

        return v;
    }

}