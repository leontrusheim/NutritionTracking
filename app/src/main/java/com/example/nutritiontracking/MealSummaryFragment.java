package com.example.nutritiontracking;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;


public class MealSummaryFragment extends Fragment {

    View inflatedView;
    ImageView iv;
    TextView tv;
    Meal meal;
    TextView ingredients;

    Uri photoURI;


    public MealSummaryFragment(Meal meal) {
        this.meal = meal;
        photoURI = meal.getUri();
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
        if (photoURI != null){
            iv.setImageURI(photoURI);
        }
        else{
            iv.setImageResource(R.drawable.default_meal);
            iv.setBackgroundColor(getResources().getColor(R.color.green_med));};
        return inflatedView;
    }
}