/*
 * @authors: Ryan Pittner & Leon Trusheim
 * @file: FoodSearchFragment.java
 * @assignment: Nutrition Tracking (Final Project)
 * @course: CSc 317 - Spring 2022 (Dicken)
 * @description: This class represents a FoodSearchFragment, a fragment that allows
 *      the user to input a name of a food ingredient, fetch API data related to the search,
 *      and display the results in the UI. This class specifies the ArrayAdapter used to
 *      place search results into the UI.
 */

package com.example.nutritiontracking;

import android.app.Activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodSearchFragment extends Fragment {
    public Activity containerActivity = null;

    public FoodSearchFragment() {
        // Required empty public constructor
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
        View v = inflater.inflate(R.layout.fragment_food_search, container, false);
        String searchTerm = getArguments().getString("searchTerm");
        EditText editText = v.findViewById(R.id.edit_food);
        editText.setText(searchTerm);
        ArrayList<Ingredient> ingredients = (ArrayList<Ingredient>) getArguments().getSerializable("ingredients");

        ArrayAdapter arrayAdapter = new ArrayAdapter (containerActivity, R.layout.food_list_row,
                R.id.food_list_row_item, ingredients) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tvHeader = (TextView) view.findViewById(R.id.food_list_row_item);
                TextView tvDetails = (TextView) view.findViewById(R.id.food_list_title);
                Ingredient curr = ingredients.get(position);

                tvHeader.setText(curr.getName());
                tvDetails.setText(curr.getBrand());
                tvHeader.setTag(position);
                tvDetails.setTag(position);

                return view;
            }
        };
        ListView listView = v.findViewById(R.id.food_list);
        listView.setAdapter(arrayAdapter);

        return v;
    }

}