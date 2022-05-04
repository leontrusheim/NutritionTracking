/*
 * @authors: Ryan Pittner & Leon Trusheim
 * @file: ProgressSelectorFragment.java
 * @assignment: Nutrition Tracking (Final Project)
 * @course: CSc 317 - Spring 2022 (Dicken)
 * @description: This class represents a ProgressSelectorFragment, a fragment that displays
 *      two buttons and allows the user to select which summary of their progress they would
 *      like to view (daily or weekly summary).
 */

package com.example.nutritiontracking;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ProgressSelectorFragment extends Fragment {
    public Activity containerActivity = null;

    public ProgressSelectorFragment() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_progress_selector, container, false);
    }
}