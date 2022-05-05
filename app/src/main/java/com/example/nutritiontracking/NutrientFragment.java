/*
 * @authors: Ryan Pittner & Leon Trusheim
 * @file: NutrientFragment.java
 * @assignment: Nutrition Tracking (Final Project)
 * @course: CSc 317 - Spring 2022 (Dicken)
 * @description: The NutrientFragment is a fragment class that displays information about the
 *               current ingredient and allows the user to add it to the meal.
 */

package com.example.nutritiontracking;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

        //Set the TextView for the nutrient
        TextView textView = v.findViewById(R.id.header);
        textView.setText(curr.getName());
        TextView tv = v.findViewById(R.id.nutrient);
        tv.setText(curr.getNutrients());

        //Set onclick to perform adding the ingredient to the meal
        Button b = v.findViewById(R.id.add_to_meal);
        b.setOnClickListener(view -> {
            meal.addIngredient(curr);
            Fragment mealFrag = new MealSummaryFragment(meal);
            FragmentTransaction fragmentTransaction = getParentFragmentManager().
                    beginTransaction().setCustomAnimations(
                    R.anim.slide_right,
                    R.anim.fade_out);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.mainContent, mealFrag);
            fragmentTransaction.commit();
        });

        return v;
    }


}