/*
 * @authors: Ryan Pittner & Leon Trusheim
 * @file: WeeklyGraphFragment.java
 * @assignment: Nutrition Tracking (Final Project)
 * @course: CSc 317 - Spring 2022 (Dicken)
 * @description: This represents a WeeklyGraphFragment, a fragment that displays
 *          a summary of the weekly nutrient intake. Its constructor takes a nutrient type
 *          (cals, carbs, fat, protein) of the nutrient summary to be displayed, a color
 *          of the graph, and a target value (the goal for that type of nutrient).
 */

package com.example.nutritiontracking;

import android.app.Activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.graphics.Color;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeeklyGraphFragment extends Fragment {

    public final String FATS = SettingsFragment.FAT;
    public final String CALS = SettingsFragment.CAL;
    public final String PROTEINS = SettingsFragment.PROTEIN;
    public final String CARBS = SettingsFragment.CARB;

    public static Date date = new Date();
    public static SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    public static String currDate = formatter.format(date);

    String nutrientType;

    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    TextView targetTV;
    ArrayList barEntriesArrayList;
    int targetValue;
    public ArrayList<String> days;
    ArrayList<Float> dataset;
    String color;
    public Activity containerActivity = null;

    /**
     *  Constructor for weekly graph fragment
     * @param nutrientType -- a string representing the type of nutrient to display
     * @param color -- a string, representing the color of the graph
     * @param target -- an int, the target goal value for that nutrient type
     */
    public WeeklyGraphFragment(String nutrientType, String color, int target) {
        this.nutrientType = nutrientType;
        this.color = color;
        targetValue = target;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weekly_graph, container, false);
        setWeeklyValues();

        targetTV = v.findViewById(R.id.target_value);
        barChart = v.findViewById(R.id.idBarChart);

        drawGraph();

        return v;
    }

    /**
     * Configures the chart's appearance including the xAxis, description display,
     * and typeface.
     */
    private void configureChartAppearance() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setNoDataTextTypeface(getResources().getFont(R.font.poppins_regular));

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return days.get((int) value);
            }
        });
    }

    /**
     * Draws the graph by setting the data set, setting the colors, setting the text,
     * then animating the graph
     */
    public void drawGraph(){
        barEntriesArrayList = new ArrayList<>();
        for (int i=0; i < days.size(); i++){
            barEntriesArrayList.add(new BarEntry(i, dataset.get(i)));
        }
        barDataSet = new BarDataSet(barEntriesArrayList, nutrientType);
        barDataSet.setValueTypeface(getResources().getFont(R.font.poppins_regular));
        configureChartAppearance();
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        LimitLine ll = new LimitLine(targetValue, "Target Value");
        ll.setTypeface(getResources().getFont(R.font.poppins_regular));
        barChart.getAxisLeft().addLimitLine(ll);
        barDataSet.setColors(Color.parseColor(color));
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        if (nutrientType.equals(SettingsFragment.CAL)){
            targetTV.setText("Target Value =  " + targetValue + " kcals");
        }
        else{targetTV.setText("Target Value =  " + targetValue + " g");}

    }

    /**
     * Get the total of each macro consumed for the past 7 days. Set the dataset
     * to contain these new values.
     */
    public void setWeeklyValues(){
        MainActivity.setCurrDate(currDate);
        MainActivity.changeCurrDate(-7);
        dataset = new ArrayList<>();
        days = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            MainActivity.changeCurrDate(1);
            ArrayList<Meal> currMeals = MainActivity.getMealsAtCurr();
            days.add(MainActivity.getCurrDate().substring(0,5));
            if (currMeals != null) {
                Float temp = 0.0f;
                for (Meal m : currMeals) {
                    switch (nutrientType) {
                        case CALS:
                            temp += m.cals;
                            break;
                        case PROTEINS:
                            temp += m.proteins;
                            break;
                        case FATS:
                            temp += m.fats;
                            break;
                        case CARBS:
                            temp += m.carbs;
                            break;
                    }
                }
                dataset.add(temp);
            }
            else {dataset.add(0.0f);}
        }
    }
}