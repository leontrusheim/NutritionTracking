package com.example.nutritiontracking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.HashMap;

public class DailyGraphFragment extends Fragment {

    // Create the object of TextView
    // and Piechart class
    TextView tvProtein, tvFat, tvCarbs, tvJava;
    PieChart pieChart;
    HashMap<String,Integer> nutrients = new HashMap<String,Integer>();
    int[] dataset;
    int total_calories;
    int proteinPercent;
    int fatPercent;
    int carbsPercent;

    public DailyGraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        Fragment dateFrag = new DateSelectorFragment();
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.dateBar, dateFrag);
        fragmentTransaction.commit();
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nutrients.put("Protein", 100);
        nutrients.put("Fat", 130);
        nutrients.put("Carbs", 200);
        total_calories = nutrients.get("Protein") * 4 + nutrients.get("Fat") * 9 +
                         nutrients.get("Carbs") * 4;
        proteinPercent = nutrients.get("Protein") * 400 / total_calories;
        fatPercent = nutrients.get("Fat") * 900 / total_calories;
        carbsPercent = nutrients.get("Carbs") * 400 / total_calories;
        dataset = new int[]{proteinPercent, fatPercent, carbsPercent};
        int diff = 100 - ((int) (proteinPercent) + (int) (fatPercent) + (int) (carbsPercent));
        for (int i=0; i < diff; i++){
            dataset[i] += 1;
        }
        View v = inflater.inflate(R.layout.fragment_daily_graph, container, false);
        tvProtein = v.findViewById(R.id.tvProtein);
        tvFat = v.findViewById(R.id.tvFat);
        tvCarbs = v.findViewById(R.id.tvCarbs);
        pieChart = v.findViewById(R.id.piechart);
        // Creating a method setData()
        // to set the text in text view and pie chart
        setData();
        // Inflate the layout for this fragment
        return v;
    }

    private void setData()
    {

        // Set the percentages
        tvProtein.setText(Integer.toString(dataset[0]));
        tvFat.setText(Integer.toString(dataset[1]));
        tvCarbs.setText(Integer.toString(dataset[2]));

        // Set the data and color to the pie chart
        pieChart.addPieSlice(
                new PieModel(
                        "Protein",
                        dataset[0],
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "Fat",
                        dataset[1],
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "Carbs",
                        dataset[2],
                        Color.parseColor("#EF5350")));

        // To animate the pie chart
        pieChart.startAnimation();
    }
}