/*
 * @authors: Ryan Pittner & Leon Trusheim
 * @file: MealSummaryFragment.java
 * @assignment: Nutrition Tracking (Final Project)
 * @course: CSc 317 - Spring 2022 (Dicken)
 * @description: Represents a MealSummaryFragment class, a class that displays meals image,
 *          ingredients, and nutritional value.
 */

package com.example.nutritiontracking;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MealSummaryFragment extends Fragment {

    View inflatedView;
    ImageView iv;
    TextView tv;
    Meal meal;
    TextView ingredients;

    Bitmap bitmap;

    public MealSummaryFragment(Meal meal) {
        this.meal = meal;
        bitmap = meal.getBitmap();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_meal_summary, container, false);
        iv = inflatedView.findViewById(R.id.meal_image);
        tv = inflatedView.findViewById(R.id.nutrient_summary);
        ingredients = inflatedView.findViewById(R.id.ingredients_list);
        ingredients.setText(meal.getIngredients());
        tv.setText(meal.getNutrients());

        // Set the image for the meal or the default image if none is saved
        if (bitmap != null){
            iv.setImageBitmap(bitmap);
        }
        else{
            iv.setImageResource(R.drawable.default_meal);
        }
        return inflatedView;
    }
}