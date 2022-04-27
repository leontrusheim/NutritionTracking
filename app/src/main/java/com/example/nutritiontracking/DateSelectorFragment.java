package com.example.nutritiontracking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DateSelectorFragment extends Fragment {

    String date;


    public DateSelectorFragment(String date) {
        this.date = date;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_date_selector, container, false);
        TextView dateTv = v.findViewById(R.id.date);
        dateTv.setText(date);
        return v;
    }
}