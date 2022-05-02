package com.example.nutritiontracking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class DailyGraphFragment extends Fragment {

    // Create the object of TextView
    // and Piechart class
    TextView tvProtein, tvFat, tvCarbs, tvJava;
    PieChart pieChart;


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
        tvProtein.setText(Integer.toString(40));
        tvFat.setText(Integer.toString(30));
        tvCarbs.setText(Integer.toString(5));

        // Set the data and color to the pie chart
        pieChart.addPieSlice(
                new PieModel(
                        "Protein",
                        Integer.parseInt(tvProtein.getText().toString()),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "Fat",
                        Integer.parseInt(tvFat.getText().toString()),
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "Carbs",
                        Integer.parseInt(tvCarbs.getText().toString()),
                        Color.parseColor("#EF5350")));

        // To animate the pie chart
        pieChart.startAnimation();
    }
}