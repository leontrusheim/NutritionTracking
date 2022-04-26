package com.example.nutritiontracking;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.net.URI;


public class MealSummaryFragment extends Fragment {

    Uri photoURI;

    public MealSummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            String uri = getArguments().getString("PATH");
            photoURI = Uri.parse(uri);}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_meal_summary, container, false);
        ImageView iv = v.findViewById(R.id.meal_image);
        iv.setImageURI(photoURI);
        return v;
    }
}