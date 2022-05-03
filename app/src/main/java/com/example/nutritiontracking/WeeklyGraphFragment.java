package com.example.nutritiontracking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class WeeklyGraphFragment extends Fragment {

    public final String FATS = "fat";
    public final String CALS = "kcal";
    public final String PROTEINS = "protein";
    public final String CARBS = "carbs";

    public static Date date = new Date();
    public static SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    public static String currDate = formatter.format(date);

    String nutrientType;

    ArrayList<Float> values;

    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntriesArrayList;
    int targetValue;
    public ArrayList<String> days;
    ArrayList<Float> dataset;
    String nutrient;
    HashMap<String,int[]> nutrients;
    String color;
    public Activity containerActivity = null;

    public WeeklyGraphFragment(String nutrientType, String color, int target) {
        this.nutrientType = nutrientType;
        this.color = color;
        targetValue = target;
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weekly_graph, container, false);
        getValues(nutrientType);

        /*
        String[] categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.dropdown_menu_item, categories);
        Spinner sp = v.findViewById(R.id.spinner);
        sp.setAdapter(arrayAdapter);
         */

        //nutrients = (HashMap<String, int[]>) getArguments().getSerializable("nutrients");

        //nutrient = getArguments().getString("nutrient");
        //color = getArguments().getString("color");
        //targetValue = getArguments().getInt("target");

        //dataset = nutrients.get(nutrient);
        barChart = v.findViewById(R.id.idBarChart);

        drawGraph();

        return v;
    }

    public void onClickChangeGraph(String nutrient){
        getValues(nutrient);
        drawGraph();
    }

    private void configureChartAppearance() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return days.get((int) value);
            }
        });
    }

    public void drawGraph(){
        barEntriesArrayList = new ArrayList<>();
        for (int i=0; i < days.size(); i++){
            barEntriesArrayList.add(new BarEntry(i, dataset.get(i)));
        }
        barDataSet = new BarDataSet(barEntriesArrayList, nutrient);
        configureChartAppearance();
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        LimitLine ll = new LimitLine(targetValue, "Target Value");
        barChart.getAxisLeft().addLimitLine(ll);
        barDataSet.setColors(Color.parseColor(color));
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);
    }

    public void getValues(String nutrient){
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
        System.out.println(dataset);
        System.out.println(days);
    }

    public void setTargetValueMarker(){

    }
}