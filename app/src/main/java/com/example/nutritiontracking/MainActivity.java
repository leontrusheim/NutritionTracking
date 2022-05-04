/*
 * @authors: Ryan Pittner & Leon Trusheim
 * @file: MainActivity.java
 * @assignment: Nutrition Tracking (Final Project)
 * @course: CSc 317 - Spring 2022 (Dicken)
 * @description: TODO TODO TODO
 */
package com.example.nutritiontracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public  class MainActivity extends AppCompatActivity {

    public static int currTab = 0;

    public static String searchTerm;

    public static ArrayList<Ingredient> ingredients = new ArrayList<>();
    public static HashMap<String, ArrayList<Meal>> meals = new HashMap<>();

    public static Date date = new Date();

    public static ArrayList<String> dates = new ArrayList<>();

    public static SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    public static String currDate = formatter.format(date);
    public static Meal currMeal;

    public boolean fileInit;
    public static final String FILE_INIT = "FILE_INIT";

    public SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkStoragePermission();
        setContentView(R.layout.activity_main);

        //Initialize file to store data if not already initialized
        sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        fileInit = sharedPrefs.getBoolean(FILE_INIT, false);
        if (! fileInit){
            saveToFile();
        }
        readFiles();

        // add menu bar at bottom of screen
        Fragment menuFrag = new MenuSelectorFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.menuBar, menuFrag);
        fragmentTransaction.commit();

        //add main menu fragment to top of screen
        Fragment homeFrag = new HomeMenuFragment();
        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction2.add(R.id.mainContent, homeFrag);
        fragmentTransaction2.commit();
    }

    /**
     * Creates a fragment to display settings and replaces the UI to display it.
     */
    public void onClickViewSettings(int animId){
        Fragment settingFrag = new SettingsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        animId,
                        R.anim.fade_out);
        fragmentTransaction.replace(R.id.mainContent, settingFrag);
        fragmentTransaction.commit();
    }

    /**
     * Creates a fragment to display settings and replaces the UI to display it.
     */
    public void onClickGetHelp(View v){
        currTab = -1;
        Fragment helpFrag = new HelpFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_up,
                        R.anim.fade_out);
        fragmentTransaction.replace(R.id.mainContent, helpFrag);
        fragmentTransaction.commit();
    }

    /**
     * Creates a fragment to display Progress menu and replaces the UI to display it.
     */
    public void onClickViewProgress(){
        ProgressSelectorFragment progressFrag = new ProgressSelectorFragment();
        progressFrag.setContainerActivity(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right,
                        R.anim.fade_out);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainContent, progressFrag);
        fragmentTransaction.commit();
    }

    /**
     * Creates a fragment for the main menu and replaces the UI to display it.
     */
    public void onClickViewHome(){
        Fragment homeFrag = new HomeMenuFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_left,
                        R.anim.fade_out);
        fragmentTransaction.replace(R.id.mainContent, homeFrag);
        fragmentTransaction.commit();
    }

    /**
     * Creates a fragment to display Adding meal photos and replaces the UI to display it.
     */
    public void onClickViewMeals(int animId){
        Fragment mealFrag = new AddMealFragment(meals.get(currDate), currDate);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        animId,
                        R.anim.fade_out);
        fragmentTransaction.replace(R.id.mainContent, mealFrag);
        fragmentTransaction.commit();
    }

    /**
     * Creates a fragment to display the meal summary and replaces the UI to display it.
     */
    public void onClickMealSummary(View v){
        Fragment mealSummaryFragment = new MealSummaryFragment(currMeal);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right,
                        R.anim.fade_out);
        fragmentTransaction.replace(R.id.mainContent, mealSummaryFragment);
        fragmentTransaction.commit();
    }
    /**
     * Creates a fragment to display search results for a food item and replaces the UI to display it.
     */
    public void OnClickSearchFoodItems(View v){
        FoodSearchFragment searchFragment = new FoodSearchFragment();
        searchFragment.setContainerActivity(this);
        Bundle args = new Bundle();
        args.putSerializable("ingredients", ingredients);
        args.putString("searchTerm", searchTerm);
        searchFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right,
                        R.anim.fade_out);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainContent, searchFragment);
        fragmentTransaction.commit();
    }

    /**
     * Puts the fragment to display a Daily Summary on the screen.
     */
    public void OnClickShowDailySummary(View v){
        currTab = -1;
        DailyGraphFragment dailyGraphFragment = new DailyGraphFragment(currDate, getMealsAtCurr());
        dailyGraphFragment.setContainerActivity(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right,
                        R.anim.fade_out);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainContent, dailyGraphFragment);
        fragmentTransaction.commit();
    }

    /**
     * Puts the fragment to display a Weekly Summary on the screen.
     */
    public void OnClickShowWeeklySummary(View v){
        currTab = -1;
        int target = sharedPrefs.getInt(SettingsFragment.CAL, 2000);
        WeeklyGraphFragment weeklyGraphFragment = new WeeklyGraphFragment("cals", "#8FDD34", target);
        weeklyGraphFragment.setContainerActivity(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right,
                        R.anim.fade_out);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainContent, weeklyGraphFragment);
        fragmentTransaction.commit();
    }

    /**
     * Updates and refreshes the weekly summary graph in order to display data
     * relating to the specified nutrient type.
     */
    public void OnClickUpdateWeeklySummary(View v){
        Button b = (Button) v;
        String nutrient = (String) b.getText();
        String color = (String) b.getTag();

        int targetCals = sharedPrefs.getInt(SettingsFragment.CAL, 2000);

        int target = sharedPrefs.getInt(nutrient, 50);
        if (nutrient.equals(SettingsFragment.CARB) || nutrient.equals(SettingsFragment.PROTEIN)){
            target = (targetCals * target) / (400);
        }
        else if (nutrient.equals(SettingsFragment.FAT)){
            target = (targetCals * target) / (900);
        }

        WeeklyGraphFragment weeklyGraphFragment = new WeeklyGraphFragment(nutrient, color, target);
        weeklyGraphFragment.setContainerActivity(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainContent, weeklyGraphFragment);
        fragmentTransaction.commit();
    }

    /**
     * Performs a search for food items relating to the search term.
     */
    public void OnClickShowFoodItems(View v){
        ingredients.clear();
        EditText input = findViewById(R.id.edit_food);
        searchTerm = String.valueOf(input.getText());
        new SearchTask().execute();
    }

    /**
     * Clears the edit text so that the user may easily type in a new search term.
     */
    public void onClickClearEditText(View v){
        EditText et = findViewById(R.id.edit_food);
        et.setText("");
    }

    /**
     * An onClick method for saving the meal. It adds teh meal to the hashmap if it is not already
     * in the hashmap, updates the AddMeal Fragment to display the new meal image, and saves
     * the meals to a text file for persistent memory.
     */
    public void onClickSaveMeal(View v){
        if (!dates.contains(currDate)) {
            dates.add(currDate);
        }
        ArrayList<Meal> dateMeals = meals.get(currDate);
        if (dateMeals == null){
            dateMeals = new ArrayList<>();
            meals.put(currDate, dateMeals);
        }
        if (!dateMeals.contains(currMeal)) {
            dateMeals.add(0, currMeal);
        }
        Fragment addMealFrag = new AddMealFragment(meals.get(currDate), currDate);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                R.anim.slide_right,
                R.anim.fade_out);
        fragmentTransaction.replace(R.id.mainContent, addMealFrag);
        fragmentTransaction.commit();
        saveToFile();
    }

    /**
     * Opens a new fragment to display the methods for how the user can add a meal picture.
     */
    public void onClickAddMeal(View v){
        currTab = -1;
        createNewMeal();

        Fragment photoFrag = new AddPhotoFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right,
                        R.anim.fade_out);
        fragmentTransaction.replace(R.id.mainContent, photoFrag);
        fragmentTransaction.commit();
    }

    /**
     * TODO
     */
    public void setFoodSearchFragment(){
        FoodSearchFragment searchFragment = new FoodSearchFragment();
        searchFragment.setContainerActivity(this);
        Bundle args = new Bundle();
        args.putSerializable("ingredients", ingredients);
        args.putString("searchTerm", searchTerm);
        searchFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainContent, searchFragment);
        fragmentTransaction.commit();
    }

    /**
     * TODO
     */
    private class SearchTask extends AsyncTask<Object, Void, JSONObject> {

        /**
         * TODO
         */
        @Override
        protected JSONObject doInBackground(Object[] objects) {
            return fetchJason();
        }

        /**
         * TODO
         */
        @Override
        protected void onPostExecute(JSONObject jsonFood) {
            try {
                JSONArray infoArray = jsonFood.getJSONArray("hits");
                for (int i = 0; i < infoArray.length(); i++){
                    JSONObject infos = infoArray.getJSONObject(i);
                    JSONObject info = infos.getJSONObject("fields");
                    Ingredient ingredient = new Ingredient(info);
                    ingredients.add(ingredient);
                }
                setFoodSearchFragment();

            } catch (JSONException e) {
                Log.d("myTag", "Error" + e + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * TODO
     */
    public JSONObject fetchJason(){
        try {
            String json = "";
            String line;
            URL url = new URL("https://api.nutritionix.com/v1_1/search/" +
                    searchTerm + "?results=0:50&fields=item_name,brand_name,nf_calories,nf_total_fat" +
                    ",nf_total_carbohydrate,nf_protein,&appId=267a1365&appKey=" +
                    "4d11e256992595b46b476dd78c395b30");
            URLConnection urlc = url.openConnection();
            urlc.setRequestProperty("user-agent", "Mozilla/5.0 " +
                    "(Macintosh; Intel Mac OS X 10_14_6)AppleWebKit/537.36 (KHTML, like Gecko)" +
                    " Chrome/85.0.4183.121 Safari/537.36 OPR/71.0.3770.284");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            while ((line = in.readLine()) != null) {
                json += line;
            }
            in.close();
            return new JSONObject(json);
        } catch (Exception e) {
            Log.d("myTag", "Error" + e + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Creates a fragment to display nutrients for a food item and replaces the UI to display it.
     */
    public void onClickShowNutrients(View v){
        int tag = (int) v.getTag();

        NutrientFragment nutrientFragment = new NutrientFragment();
        nutrientFragment.setContainerActivity(this);
        Bundle args = new Bundle();

        // get and put the ingredient into the bundle
        args.putSerializable("ingredients", new Ingredient[]{ingredients.get(tag)});
        nutrientFragment.setArguments(args);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_right,
                        R.anim.fade_out);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainContent, nutrientFragment);
        fragmentTransaction.commit();
    }

    /**
     * Sets the MainActivity's current meal to the meal passed
     */
    public static void setCurrMeal(Meal currMeal) {
        MainActivity.currMeal = currMeal;
    }

    /**
     * Creates a new meal object and sets the current meal to it
     */
    public static void createNewMeal(){
        currMeal = new Meal();
    }

    /**
     * Gets the current date as a string (current as in which date the MainActivity is
     * currently viewing, not the actual current date)
     */
    public static String getCurrDate() {
        return currDate;
    }

    /**
     * Sets the current date to the string date passed
     */
    public static void setCurrDate(String currDate) {
        MainActivity.currDate = currDate;
    }


    /**
     * Saves the meal data to a text file by iterating through the meals for each day,
     * and storing the data as a string.
     */
    public void saveToFile(){
        editor.putBoolean(FILE_INIT, true);
        editor.commit();
        String temp;
        temp = "{\"dates\":[";
        ArrayList<String> dateStrs = new ArrayList<>();
        for (String date: dates){
            String dStr = "{\"date\":" + "\"" + date + "\",\"meals\":[";
            ArrayList<Meal> currMeals = meals.get(date);
            ArrayList<String> mealStr = new ArrayList<>();
            for (Meal m : currMeals){
                mealStr.add(m.toString());
            }
            dStr += String.join(",",mealStr);
            dStr += "]}";
            dateStrs.add(dStr);
        }

        temp += String.join(",", dateStrs) + "]}";

        String filename = "data.txt";
        String fileContents = temp;
        try {
            FileOutputStream fos = getBaseContext().openFileOutput(filename, MODE_PRIVATE);
            fos.write(fileContents.getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Converts string data (from a text file) into Meal objects and populates
     * the arraylists in the MainActivity class with these meals.
     */
    public void stringToMeals(String s){
        JSONObject obj = null;
        try{obj = new JSONObject(s);
            JSONArray jsonDates = obj.getJSONArray("dates");
            for (int i = 0; i < jsonDates.length(); i++){
                JSONObject date = jsonDates.getJSONObject(i);
                String dateVal = date.getString("date");
                dates.add(dateVal);
                ArrayList<Meal> newMealList = new ArrayList<>();
                JSONArray jsonMeals = date.getJSONArray("meals");
                for (int j = 0; j < jsonMeals.length(); j++){
                    JSONObject meal = jsonMeals.getJSONObject(j);
                    Meal newMeal = new Meal(meal);
                    newMealList.add(newMeal);
                }
                meals.put(dateVal, newMealList);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Reads meal data saved to a text file.
     */
    public void readFiles() {
        FileInputStream fis = null;
        try {
            fis = getBaseContext().openFileInput("data.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
        } finally {
            String contents = stringBuilder.toString();
            stringToMeals(contents);
        }
    }

    /**
     * Returns an ArrayList of meals from the current date
     * @return
     */
    public static ArrayList<Meal> getMealsAtCurr(){
        return meals.get(currDate);
    }

    /**
     * Increases or decreases the current date by inc number of days
     * @param inc -- an int, the number of days to increase the current date by.
     */
    public static void changeCurrDate(int inc){
        int newDate = MainActivity.date.getDate() + inc;
        date.setDate(newDate);
        currDate = formatter.format(date);
    }

    /**
     * Main onClick method for switching tabs in the UI. This function determines which
     * tab the user is navigate from and to, sets the animation type (left or right) to
     * be displayed, then calls the appropriate method in order to change the tab.
     */
    public void onClickChangeTabAnim(View v){
        int goHere = Integer.parseInt((String) v.getTag());
        int animId = 0;
        if (goHere > currTab){
            animId = R.anim.slide_right;
        }
        else if (goHere < currTab){
            animId = R.anim.slide_left;
        }
        else{return;}
        currTab = goHere;
        switch (goHere){
            case 0:
                onClickViewHome();
                break;
            case 1:
                onClickViewSettings(animId);
                break;
            case 2:
                onClickViewMeals(animId);
                break;
            case 3:
                onClickViewProgress();
                break;
        }
    }

    /**
     * Automatically requests read and write to external storage when app is first opened. This allows the user
     * to use a content provider to display images without it crashing.
     */
    public void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            // Ask for the permission
            requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 100);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            // Ask for the permission
            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 100);
        }
    }

}