package com.example.nutritiontracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public  class MainActivity extends AppCompatActivity {

    public static String CARBS = "carbs";

    /**
     * Testing header blah blah blah
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment menuFrag = new MenuSelectorFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.menuBar, menuFrag);
        fragmentTransaction.commit();

        Fragment homeFrag = new HomeMenuFragment();
        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.add(R.id.mainContent, homeFrag);
        fragmentTransaction2.commit();
    }

    public void onClickViewSettings(View v){
        Fragment settingFrag = new SettingsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, settingFrag);
        fragmentTransaction.commit();
    }

    public void onClickViewProgress(View v){
        Fragment progressFrag = new ProgressSelectorFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, progressFrag);
        fragmentTransaction.commit();
    }

    public void onClickViewHome(View v){
        Fragment homeFrag = new HomeMenuFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, homeFrag);
        fragmentTransaction.commit();
    }

    public void onClickViewMeals(View v){
        Fragment mealFrag = new AddMealFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, mealFrag);
        fragmentTransaction.commit();
    }

    public void onClickMealSummary(View v){
        Fragment mealSummaryFragment = new MealSummaryFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, mealSummaryFragment);
        fragmentTransaction.commit();
    }

    public void onClickSearchFood(View v){
        Fragment searchFragment = new FoodSearchFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, searchFragment);
        fragmentTransaction.commit();
    }

}