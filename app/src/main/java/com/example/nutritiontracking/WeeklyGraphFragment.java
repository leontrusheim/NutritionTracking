package com.example.nutritiontracking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;

public class WeeklyGraphFragment extends Fragment {

    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntriesArrayList;
    int targetValue = 6;
    private static final String[] DAYS = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};


    public WeeklyGraphFragment() {
        // Required empty public constructor
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

        /*
        String[] categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.dropdown_menu_item, categories);
        Spinner sp = v.findViewById(R.id.spinner);
        sp.setAdapter(arrayAdapter);
         */
        barChart = v.findViewById(R.id.idBarChart);
        barEntriesArrayList = new ArrayList<>();
        barEntriesArrayList.add(new BarEntry(0f, 4));
        barEntriesArrayList.add(new BarEntry(1f, 6));
        barEntriesArrayList.add(new BarEntry(2f, 8));
        barEntriesArrayList.add(new BarEntry(3f, 2));
        barEntriesArrayList.add(new BarEntry(4f, 4));
        barEntriesArrayList.add(new BarEntry(5f, 1));
        barEntriesArrayList.add(new BarEntry(6f, 1));
        barDataSet = new BarDataSet(barEntriesArrayList, "Weekly Nutrients");
        configureChartAppearance();
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        int maxCapacity = 100;
        LimitLine ll = new LimitLine(targetValue, "Target Value");
        barChart.getAxisLeft().addLimitLine(ll);
        barDataSet.setColors(getResources().getColor(R.color.green_dark));
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);
        return v;
    }

    private void configureChartAppearance() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DAYS[(int) value];
            }
        });
    }
}