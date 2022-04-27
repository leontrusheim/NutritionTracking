package com.example.nutritiontracking;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class NutrientFragment extends Fragment {
    public Activity containerActivity = null;
    public Ingredient curr;
    Meal meal = MainActivity.currMeal;

    public NutrientFragment() {
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
        View v = inflater.inflate(R.layout.fragment_nutrient, container, false);
        Ingredient[] ingredients = (Ingredient[]) getArguments().getSerializable("ingredients");
        curr = ingredients[0];
        TextView textView = v.findViewById(R.id.header);
        textView.setText(curr.getName());
        TextView tv = v.findViewById(R.id.nutrient);
        tv.setText(curr.getNutrients());

        Button b = v.findViewById(R.id.add_to_meal);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meal.addIngredient(curr);
                Fragment mealFrag = new MealSummaryFragment(meal);
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainContent, mealFrag);
                fragmentTransaction.commit();
            }
        });

        return v;
    }


}