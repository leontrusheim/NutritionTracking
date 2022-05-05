/*
 * @authors: Ryan Pittner & Leon Trusheim
 * @file: DailyGraphFragment.java
 * @assignment: Nutrition Tracking (Final Project)
 * @course: CSc 317 - Spring 2022 (Dicken)
 * @description: This class represents a DailyGraph Fragment, a fragment to display
 *      a pie chart of the nutrients consumed for a specific day. The fragment sets
 *      up the graphical representation, gets the data necessary for displaying,
 *      and updates the UI.
 */

package com.example.nutritiontracking;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

public class DailyGraphFragment extends Fragment {

    public SharedPreferences sharedPrefs;

    ArrayList<Meal> meals;
    String date;
    View invokerView;

    TextView tvProtein, tvFat, tvCarbs, tvDate;
    PieChart pieChart;
    int[] dataset;

    int cals, protein, fat, carbs;

    int proteinPercent = 0;
    int fatPercent = 0;
    int carbsPercent = 0;
    public Activity containerActivity = null;


    /**
     * Constructor for DailyGraphFragment
     * @param date -- the date to display
     * @param meals -- an ArrayList of Meal objects for the day
     */
    public DailyGraphFragment(String date, ArrayList<Meal> meals) {
        this.date = date;
        this.meals = meals;
    }

    /**
     * Setter for containerActivity
     */
    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        invokerView = inflater.inflate(R.layout.fragment_daily_graph, container, false);

        //set the TextViews
        tvProtein = invokerView.findViewById(R.id.tvProtein);
        tvFat = invokerView.findViewById(R.id.tvFat);
        tvCarbs = invokerView.findViewById(R.id.tvCarbs);
        tvDate = invokerView.findViewById(R.id.date_daily_summary);
        pieChart = invokerView.findViewById(R.id.piechart);

        //Set Text to show the goals
        TextView tvCarbGoal =  invokerView.findViewById(R.id.summary_carbs);
        TextView tvFatGoal =  invokerView.findViewById(R.id.summary_fats);
        TextView tvProteinGoal =  invokerView.findViewById(R.id.summary_proteins);
        tvCarbGoal.setText("Carbs (Goal = " + getCarbPercent() + "%)");
        tvFatGoal.setText("Fats (Goal = " + getFatPercent() + "%)");
        tvProteinGoal.setText("Proteins (Goal = " + getProteinPercent() + "%)");

        TextView backButton = invokerView.findViewById(R.id.yesterday);
        TextView forwardButton = invokerView.findViewById(R.id.tomorrow);
        View.OnClickListener listener = view -> {
            onClickChangeDate(view);
            setGraphAndText();
        };
        backButton.setOnClickListener(listener);
        forwardButton.setOnClickListener(listener);

        setDailyNutrients();

        setData();

        return invokerView;
    }

    /**
     * Sets the graph data in order to update the UI. Adds slices to the pie chart and animates it.
     * Sets the text summary below the graph.
     */
    private void setData() {

        tvDate.setText(date);
        dataset = new int[]{proteinPercent, fatPercent, carbsPercent};
        pieChart.clearChart();

        if (cals == 0){ // if there is not meal data for that day
            invokerView.findViewById(R.id.carb_color).setBackgroundColor(getResources()
                    .getColor(R.color.light_gray));
            invokerView.findViewById(R.id.protein_color).setBackgroundColor(getResources()
                    .getColor(R.color.light_gray));
            invokerView.findViewById(R.id.fat_color).setBackgroundColor(getResources()
                    .getColor(R.color.light_gray));
            tvProtein.setText("");
            tvCarbs.setText("");
            tvFat.setText("");

            pieChart.addPieSlice(//add a blank slice
                    new PieModel("null", 100, getResources()
                            .getColor(R.color.white)));
        }
        else { //there is meal data
            invokerView.findViewById(R.id.carb_color).setBackgroundColor(getResources()
                    .getColor(R.color.carbs));
            invokerView.findViewById(R.id.protein_color).setBackgroundColor(getResources()
                    .getColor(R.color.protein));
            invokerView.findViewById(R.id.fat_color).setBackgroundColor(getResources()
                    .getColor(R.color.fat));

            int diff = 100 - ((proteinPercent) + (fatPercent) + (carbsPercent));
            for (int i = 0; i < diff; i++) {
                dataset[i%3] += 1;
            }

            // Set the data and color to the pie chart
            pieChart.addPieSlice(
                    new PieModel("Protein", dataset[0], getResources()
                            .getColor(R.color.protein)));
            pieChart.addPieSlice(
                    new PieModel("Fat", dataset[1], getResources()
                            .getColor(R.color.fat)));
            pieChart.addPieSlice(
                    new PieModel("Carbs", dataset[2], getResources()
                            .getColor(R.color.carbs)));

            pieChart.startAnimation();

            //Sets text below the graph
            tvProtein.setText(dataset[0] + " %");
            tvFat.setText(dataset[1] + " %");
            tvCarbs.setText(dataset[2] + " %");
        }
    }

    /**
     * Iterate through each meal for the day, and sets the values for TOTAL cals,
     * fats, carbs, and proteins for the day.
     */
    public void setDailyNutrients(){
        cals = 0;
        fat = 0;
        carbs = 0;
        protein = 0;
        proteinPercent = 0;
        fatPercent = 0;
        carbsPercent = 0;
        if (meals == null){return;}
        for (Meal m : meals){
            cals += m.cals;
            fat += m.fats;
            carbs += m.carbs;
            protein += m.proteins;
        }
        if (cals < 1){return;}
            proteinPercent = protein * 400 / cals;
            fatPercent = fat * 900 / cals;
            carbsPercent = carbs * 400 / cals;
    }


    /**
     * A method that changes the date of the daily graph displayed.
     */
    public void onClickChangeDate(View v){
        if (v.getId() == R.id.tomorrow){
            MainActivity.changeCurrDate(1);
        }
        else{
            MainActivity.changeCurrDate(-1);
        }
    }

    /**
     * A method to reload the daily graph. It sets the date of the fragment,
     * gets the current meals from that date, and calls the methods to get the
     * nutrients from that date and reset the data.
     */
    public void setGraphAndText(){
        date = MainActivity.getCurrDate();
        meals = MainActivity.getMealsAtCurr();
        setDailyNutrients();
        setData();
    }

    /**
     * returns the goal percent carbs from saved in the shared prefs
     */
    public int getCarbPercent(){
        return sharedPrefs.getInt(SettingsFragment.CARB, 30);
    }

    /**
     * returns the goal percent fats from saved in the shared prefs
     */
    public int getFatPercent(){
        return sharedPrefs.getInt(SettingsFragment.FAT, 20);
    }

    /**
     * returns the goal percent proteins from saved in the shared prefs
     */
    public int getProteinPercent(){
        return sharedPrefs.getInt(SettingsFragment.PROTEIN, 50);
    }
}