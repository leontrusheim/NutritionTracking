package com.example.nutritiontracking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class WeeklyGraphFragment extends Fragment {


    public WeeklyGraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_weekly_graph, container, false);
        String[] categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.dropdown_menu_item, categories);
        Spinner sp = v.findViewById(R.id.spinner);
        sp.setAdapter(arrayAdapter);
        return v;
    }
}