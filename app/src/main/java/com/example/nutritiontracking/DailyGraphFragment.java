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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class DailyGraphFragment extends Fragment {

    public SharedPreferences sharedPrefs;

    ArrayList<Meal> meals;
    String date;
    View invokerView;

    // Create the object of TextView
    // and Piechart class
    TextView tvProtein, tvFat, tvCarbs, tvDate;
    PieChart pieChart;
    HashMap<String,Integer> nutrients = new HashMap<String,Integer>();
    int[] dataset;

    int cals;
    int protein;
    int fat;
    int carbs;

    int total_calories = 0;
    int proteinPercent = 0;
    int fatPercent = 0;
    int carbsPercent = 0;
    public Activity containerActivity = null;


    public DailyGraphFragment(String date, ArrayList<Meal> meals) {
        this.date = date;
        this.meals = meals;
    }

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        /*Fragment dateFrag = new DateSelectorFragment(MainActivity.currDate);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.dateBar, dateFrag);
        fragmentTransaction.commit();*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        invokerView = inflater.inflate(R.layout.fragment_daily_graph, container, false);
        tvProtein = invokerView.findViewById(R.id.tvProtein);
        tvFat = invokerView.findViewById(R.id.tvFat);
        tvCarbs = invokerView.findViewById(R.id.tvCarbs);
        tvDate = invokerView.findViewById(R.id.date_daily_summary);
        pieChart = invokerView.findViewById(R.id.piechart);

        TextView tvCarbGoal =  invokerView.findViewById(R.id.summary_carbs);
        TextView tvFatGoal =  invokerView.findViewById(R.id.summary_fats);
        TextView tvProteinGoal =  invokerView.findViewById(R.id.summary_proteins);
        tvCarbGoal.setText("Carbs (Goal = " + getCarbPercent() + "%)");
        tvFatGoal.setText("Fats (Goal = " + getFatPercent() + "%)");
        tvProteinGoal.setText("Proteins (Goal = " + getProteinPercent() + "%)");

        TextView backButton = invokerView.findViewById(R.id.yesterday);
        TextView forwardButton = invokerView.findViewById(R.id.tomorrow);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChangeDate(view);
                setGraphAndText();
            }
        };
        backButton.setOnClickListener(listener);
        forwardButton.setOnClickListener(listener);

        //nutrients = (HashMap<String, Integer>) getArguments().getSerializable("nutrients");
        setDailyNutrients();

        //total_calories = nutrients.get("Protein") * 4 + nutrients.get("Fat") * 9 + nutrients.get("Carbs") * 4;
        //proteinPercent = nutrients.get("Protein") * 400 / total_calories;
        //fatPercent = nutrients.get("Fat") * 900 / total_calories;
        //carbsPercent = nutrients.get("Carbs") * 400 / total_calories;

        // Creating a method setData()
        // to set the text in text view and pie chart
        setData();
        // Inflate the layout for this fragment

        return invokerView;
    }

    private void setData() {

        tvDate.setText(date);
        dataset = new int[]{proteinPercent, fatPercent, carbsPercent};
        pieChart.clearChart();

        if (cals == 0){
            invokerView.findViewById(R.id.carb_color).setBackgroundColor(getResources().getColor(R.color.light_gray));
            invokerView.findViewById(R.id.protein_color).setBackgroundColor(getResources().getColor(R.color.light_gray));
            invokerView.findViewById(R.id.fat_color).setBackgroundColor(getResources().getColor(R.color.light_gray));
            tvProtein.setText("");
            tvCarbs.setText("");
            tvFat.setText("");

            pieChart.addPieSlice(
                    new PieModel(
                            "null",
                            100,
                            getResources().getColor(R.color.white)));
        }
        else {
            invokerView.findViewById(R.id.carb_color).setBackgroundColor(getResources().getColor(R.color.carbs));
            invokerView.findViewById(R.id.protein_color).setBackgroundColor(getResources().getColor(R.color.protein));
            invokerView.findViewById(R.id.fat_color).setBackgroundColor(getResources().getColor(R.color.fat));

            System.out.println("PROTEIN: " + proteinPercent);
            System.out.println("FAT: " + fatPercent);
            System.out.println("CARBS: " + carbsPercent);
            int diff = 100 - ((int) (proteinPercent) + (int) (fatPercent) + (int) (carbsPercent));
            System.out.println("DIFF: " + diff);
            for (int i = 0; i < diff; i++) {
                dataset[i%3] += 1;
            }

            // Set the percentages and date


            // Set the data and color to the pie chart
            pieChart.addPieSlice(
                    new PieModel(
                            "Protein",
                            dataset[0],
                            getResources().getColor(R.color.protein)));
            pieChart.addPieSlice(
                    new PieModel(
                            "Fat",
                            dataset[1],
                            getResources().getColor(R.color.fat)));
            pieChart.addPieSlice(
                    new PieModel(
                            "Carbs",
                            dataset[2],
                            getResources().getColor(R.color.carbs)));


            // To animate the pie chart
            pieChart.startAnimation();
            tvProtein.setText(Integer.toString(dataset[0]) + " %");
            tvFat.setText(Integer.toString(dataset[1]) + " %");
            tvCarbs.setText(Integer.toString(dataset[2]) + " %");
        }
    }

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

    public void onClickChangeDate(View v){
        if (v.getId() == R.id.tomorrow){
            MainActivity.changeCurrDate(1);
        }
        else{
            MainActivity.changeCurrDate(-1);
        }
    }

    public void setGraphAndText(){
        date = MainActivity.getCurrDate();
        meals = MainActivity.getMealsAtCurr();
        setDailyNutrients();
        setData();
    }

    public int getCarbPercent(){
        return sharedPrefs.getInt(SettingsFragment.CARB, 30);
    }


    public int getFatPercent(){
        return sharedPrefs.getInt(SettingsFragment.FAT, 20);
    }

    public int getProteinPercent(){
        return sharedPrefs.getInt(SettingsFragment.PROTEIN, 50);
    }
}