package com.example.nutritiontracking;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AddMealFragment extends Fragment {

    ArrayList<Meal> meals;
    GridLayout gl;
    int width;
    String date;

    public AddMealFragment(ArrayList<Meal> meals, String date) {
        this.meals = meals;
        this.date = date;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Put the date selector fragment within the UI of this fragment
        Fragment dateFrag = new DateSelectorFragment(MainActivity.currDate);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.topBar, dateFrag);
        fragmentTransaction.commit();
    }


    @Override
    public void onResume() {
        super.onResume();
        displayImages(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_meal, container, false);
        getWidthInPixels(v);
        gl = v.findViewById(R.id.meals_grid);
        return v;
    }

    public void displayImages(Activity context){
        if (meals != null){
            for (Meal m : meals) {
                ImageView iv = new ImageView(context);
                if (m.getUri() != null){
                    iv.setImageURI(m.getUri());
                }
                else {iv.setImageDrawable(getResources().getDrawable(R.drawable.default_meal));}

                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, width);
                iv.setLayoutParams(lp);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.setCurrMeal(m);
                        Fragment mealSummaryFrag = new MealSummaryFragment(m);
                        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainContent, mealSummaryFrag);
                        fragmentTransaction.commit();
                    }
                });
                gl.addView(iv);
            }
        }
    }


    protected void getWidthInPixels(View v) {
        int offset = v.getWidth();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = ((displayMetrics.widthPixels - offset)/ 3) - 50;
    }
}