package com.example.nutritiontracking;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AddMealFragment extends Fragment {

    ArrayList<Meal> meals;
    GridLayout gl;
    int width;
    String date;
    View invokerView;

    public AddMealFragment(ArrayList<Meal> meals, String date) {
        this.meals = meals;
        this.date = date;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Put the date selector fragment within the UI of this fragment
        /*Fragment dateFrag = new DateSelectorFragment(MainActivity.currDate);
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.topBar, dateFrag);
        fragmentTransaction.commit();*/
    }

    @Override
    public void onResume() {
        super.onResume();
        displayImagesAndText(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        invokerView = inflater.inflate(R.layout.fragment_add_meal, container, false);
        getWidthInPixels(invokerView);
        gl = invokerView.findViewById(R.id.meals_grid);
        TextView tv1 = invokerView.findViewById(R.id.forward);
        TextView tv2 = invokerView.findViewById(R.id.backward);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickChangeDate(view);
                reloadAddMealPage();
            }
        };
        tv2.setOnClickListener(listener);
        tv1.setOnClickListener(listener);

        return invokerView;
    }

    public void displayImagesAndText(Activity context){
        TextView tv = invokerView.findViewById(R.id.date_add_meal);
        tv.setText(date);
        gl.removeAllViews();
        if (meals != null){
            for (Meal m : meals) {
                ImageView iv = new ImageView(context);
                if (m.getBitmap() != null){
                    iv.setImageBitmap(m.getBitmap());
                }
                else if (m.getUri() != null){
                    iv.setImageURI(m.getUri());
                }
                else {
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.default_meal));
                }

                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, width);
                iv.setLayoutParams(lp);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.setCurrMeal(m);
                        Fragment mealSummaryFrag = new MealSummaryFragment(m);
                        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.mainContent, mealSummaryFrag);
                        fragmentTransaction.commit();
                    }
                });
                gl.addView(iv);
            }
        }
    }


    protected void getWidthInPixels(View v) {
        int offset = v.getWidth();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = ((displayMetrics.widthPixels - offset)/ 3) - 50;
    }

    public void reloadAddMealPage(){
        date = MainActivity.getCurrDate();
        meals = MainActivity.getMealsAtCurr();
        displayImagesAndText(getActivity());
    }

    public void onClickChangeDate(View v){
        if (v.getId() == R.id.forward){
            MainActivity.changeCurrDate(1);
        }
        else{
            MainActivity.changeCurrDate(-1);
        }
    }
}