/*
 * @authors: Ryan Pittner & Leon Trusheim
 * @file: MenuSelectorFragment.java
 * @assignment: Nutrition Tracking (Final Project)
 * @course: CSc 317 - Spring 2022 (Dicken)
 * @description: Represents a MenuSelectorFragment, a fragment that contains buttons for
 *          each task a user can perform on the app.
 */

package com.example.nutritiontracking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuSelectorFragment extends Fragment {

    public MenuSelectorFragment() {
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
        return inflater.inflate(R.layout.fragment_menu_selector, container, false);
    }
}