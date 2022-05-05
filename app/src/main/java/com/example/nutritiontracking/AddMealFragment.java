/*
 * @authors: Ryan Pittner & Leon Trusheim
 * @file: AddMealFragment.java
 * @assignment: Nutrition Tracking (Final Project)
 * @course: CSc 317 - Spring 2022 (Dicken)
 * @description: This class represents a fragment that allows the user to view
 *      meals for specified days. The fragment loads in Meal objects for the specified day,
 *      builds ImageViews for each meal, and sets onClick methods for how the user can interact
 *      with these images and the buttons on the screen.
 */

package com.example.nutritiontracking;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AddMealFragment extends Fragment {

    ArrayList<Meal> meals;
    GridLayout gl;
    int width;
    String date;
    View invokerView;

    /**
     * Constructor of the class taking in an ArrayList of Meals and a String date.
     */
    public AddMealFragment(ArrayList<Meal> meals, String date) {
        this.meals = meals;
        this.date = date;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        displayImagesAndText(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        invokerView = inflater.inflate(R.layout.fragment_add_meal, container, false);
        getWidthInPixels(invokerView);
        gl = invokerView.findViewById(R.id.meals_grid);
        TextView tv1 = invokerView.findViewById(R.id.forward);
        TextView tv2 = invokerView.findViewById(R.id.backward);
        View.OnClickListener listener = view -> {
            onClickChangeDate(view);
            reloadAddMealPage();
        };
        tv2.setOnClickListener(listener);
        tv1.setOnClickListener(listener);

        return invokerView;
    }

    /**
     * Update the UI to add images views for each meal and update the text w/ the date
     */
    public void displayImagesAndText(Activity context){
        //update the date
        TextView tv = invokerView.findViewById(R.id.date_add_meal);
        tv.setText(date);

        //add meal images
        gl.removeAllViews();
        if (meals != null){
            for (Meal m : meals) {
                ImageView iv = new ImageView(context);
                if (m.getBitmap() != null){
                    iv.setImageBitmap(m.getBitmap());
                }
                else {
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.default_meal));
                }

                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, width);
                iv.setLayoutParams(lp);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setOnClickListener(view -> {
                    MainActivity.setCurrMeal(m);
                    Fragment mealSummaryFrag = new MealSummaryFragment(m);
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mainContent, mealSummaryFrag);
                    fragmentTransaction.commit();
                });
                gl.addView(iv);
            }
        }
    }


    /**
     * Set the width variable to be equal to the width of the screen / 3.
     * This defines how large the images are displayed so
     * that a column of 3 images will fill the width.
     */
    protected void getWidthInPixels(View v) {
        int offset = v.getWidth();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = ((displayMetrics.widthPixels - offset)/ 3) - 50;
    }

    /**
     * Reloads the meal page for a new date
     */
    public void reloadAddMealPage(){
        date = MainActivity.getCurrDate();
        meals = MainActivity.getMealsAtCurr();
        displayImagesAndText(getActivity());
    }

    /**
     * Changes the date, forward, or backward, based on which button is clicked on
     * @param v -- the button that is clicked to navigate through the dates
     */
    public void onClickChangeDate(View v){
        if (v.getId() == R.id.forward){
            MainActivity.changeCurrDate(1);
        }
        else{
            MainActivity.changeCurrDate(-1);
        }
    }
}