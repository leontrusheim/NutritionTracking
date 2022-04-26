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

    View inflatedView;
    ImageView iv;

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
        inflatedView = inflater.inflate(R.layout.fragment_meal_summary, container, false);
        iv = inflatedView.findViewById(R.id.meal_image);
        if (photoURI != null){
            iv.setImageURI(photoURI);
        }
        else{
            iv.setImageResource(R.drawable.default_meal);
            iv.setBackgroundColor(getResources().getColor(R.color.green_med));};
        return inflatedView;
    }
}