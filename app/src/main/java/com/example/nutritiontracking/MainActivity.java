package com.example.nutritiontracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
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

    public static String searchTerm;

    public static ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
    public static HashMap<String, ArrayList<Meal>> meals = new HashMap<>();

    public static Date date = new Date();

    public static ArrayList<String> dates = new ArrayList<String>();

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
    public void onClickViewSettings(View v){
        Fragment settingFrag = new SettingsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, settingFrag);
        fragmentTransaction.commit();
    }

    /**
     * Creates a fragment to display Progress menu and replaces the UI to display it.
     */
    public void onClickViewProgress(View v){
        ProgressSelectorFragment progressFrag = new ProgressSelectorFragment();
        progressFrag.setContainerActivity(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainContent, progressFrag);
        fragmentTransaction.commit();
    }

    /**
     * Creates a fragment for the main menu and replaces the UI to display it.
     */
    public void onClickViewHome(View v){
        Fragment homeFrag = new HomeMenuFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, homeFrag);
        fragmentTransaction.commit();
    }

    /**
     * Creates a fragment to display Adding meal photos and replaces the UI to display it.
     */
    public void onClickViewMeals(View v){
        Fragment mealFrag = new AddMealFragment(meals.get(currDate), currDate);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, mealFrag);
        fragmentTransaction.commit();
    }

    /**
     * Creates a fragment to display the meal summary and replaces the UI to display it.
     */
    public void onClickMealSummary(View v){
        Fragment mealSummaryFragment = new MealSummaryFragment(currMeal);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, mealSummaryFragment);
        fragmentTransaction.commit();
    }
    /**
     * Creates a fragment to display search results for a food item and replaces the UI to display it.
     */
    public void OnClickSearchFoodItems(View v){
        setFoodSearchFragment();
    }

    public void OnClickShowDailySummary(View v){
        //HashMap nutrientVals = new HashMap<String, int[]>();
        //nutrientVals.put("Protein", 100);
        //nutrientVals.put("Fat", 130);
        //nutrientVals.put("Carbs", 200);
        DailyGraphFragment dailyGraphFragment = new DailyGraphFragment(currDate, getMealsAtCurr());
        dailyGraphFragment.setContainerActivity(this);
        //Bundle args = new Bundle();
        //args.putSerializable("day", meals.get(currDate));
        //args.putSerializable("nutrients", nutrientVals);
        //dailyGraphFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainContent, dailyGraphFragment);
        fragmentTransaction.commit();
    }

    public void OnClickShowWeeklySummary(View v){
        //HashMap nutrientVals = new HashMap<String, int[]>();
        //int [] values = new int[]{100,150,200,50,75,300,125};
        //nutrientVals.put("Kcal", values);
        //nutrientVals.put("Protein", values);
        //nutrientVals.put("Fat", values);
        //nutrientVals.put("Carbs", values);
        //int target = 300;
        int target = sharedPrefs.getInt(SettingsFragment.CAL, 2000);
        WeeklyGraphFragment weeklyGraphFragment = new WeeklyGraphFragment("kcal", "#8FDD34", target);
        weeklyGraphFragment.setContainerActivity(this);
        //Bundle args = new Bundle();
        //args.putString("nutrient","Kcal");
        //String color = "#8FDD34";
        //args.putString("color", color);
        //args.putSerializable("nutrients", nutrientVals);
        //args.putInt("target", target);
        //weeklyGraphFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainContent, weeklyGraphFragment);
        fragmentTransaction.commit();
    }

    public void OnClickUpdateWeeklySummary(View v){
        //HashMap nutrientVals = new HashMap<String, int[]>();
        //int [] values = new int[]{100,150,200,50,75,300,125};
        //int target = 300;
        //nutrientVals.put("Kcal", values);
        //nutrientVals.put("Protein", values);
        //nutrientVals.put("Fat", values);
        //nutrientVals.put("Carbs", values);
        Button b = (Button) v;
        String nutrient = (String) b.getText();
        System.out.println(nutrient);
        String color = (String) b.getTag();

        int targetCals = sharedPrefs.getInt(SettingsFragment.CAL, 2000);

        int target = sharedPrefs.getInt(nutrient, 50);
        if (nutrient.equals(SettingsFragment.CARB) || nutrient.equals(SettingsFragment.PROTEIN)){
            target = (int) (targetCals * target) / (400 * 100);
        }
        else if (nutrient.equals(SettingsFragment.FAT)){
            target = (int) (targetCals * target) / (900 * 100);
        }

        WeeklyGraphFragment weeklyGraphFragment = new WeeklyGraphFragment(nutrient, color, target);
        weeklyGraphFragment.setContainerActivity(this);
        //Bundle args = new Bundle();
        //Button b = (Button) v;

        //String nutrient = (String) b.getText();
        //args.putString("nutrient",nutrient);
        //args.putString("color", color);
        //args.putSerializable("nutrients", nutrientVals);
        //args.putInt("target", target);
        //weeklyGraphFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainContent, weeklyGraphFragment);
        fragmentTransaction.commit();
    }


    public void OnClickShowFoodItems(View v){
        ingredients.clear();
        EditText input = findViewById(R.id.edit_food);
        searchTerm = String.valueOf(input.getText());
        new SearchTask().execute();
    }

    public void onClickClearEditText(View v){
        EditText et = findViewById(R.id.edit_food);
        et.setText("");
    }

    public void onClickSaveMeal(View v){
        if (!dates.contains(currDate)) {
            dates.add(currDate);
        }
        ArrayList<Meal> dateMeals = meals.get(currDate);
        if (dateMeals == null){
            dateMeals = new ArrayList<Meal>();
            meals.put(currDate, dateMeals);
        }
        if (!dateMeals.contains(currMeal)) {
            dateMeals.add(0, currMeal);
        }
        System.out.println(currMeal.toString());
        Fragment addMealFrag = new AddMealFragment(meals.get(currDate), currDate);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, addMealFrag);
        fragmentTransaction.commit();
        saveToFile();
    }


    public void onClickAddMeal(View v){
        createNewMeal();

        Fragment photoFrag = new AddPhotoFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, photoFrag);
        fragmentTransaction.commit();
    }

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

    private class SearchTask extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object[] objects) {
            return fetchJason();
        }

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
                System.out.println("JSON LINE " + line);
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

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainContent, nutrientFragment);
        fragmentTransaction.commit();
    }

    public static void setCurrMeal(Meal currMeal) {
        MainActivity.currMeal = currMeal;
    }

    public static void createNewMeal(){
        Meal newMeal = new Meal();
        currMeal = newMeal;
    }


    public static String getCurrDate() {
        return currDate;
    }

    public static void setCurrDate(String currDate) {
        MainActivity.currDate = currDate;
    }

    public void reloadDailyPage(){}

    public void saveToFile(){
        editor.putBoolean(FILE_INIT, true);
        editor.commit();
        String temp;
        temp = "{\"dates\":[";
        ArrayList<String> dateStrs = new ArrayList<String>();
        for (String date: dates){
            String dStr = "{\"date\":" + "\"" + date + "\",\"meals\":[";
            ArrayList<Meal> currMeals = meals.get(date);
            ArrayList<String> mealStr = new ArrayList<String>();
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

    public void stringToMeals(String s){
        JSONObject obj = null;
        try{obj = new JSONObject(s);
            JSONArray jsonDates = obj.getJSONArray("dates");
            for (int i = 0; i < jsonDates.length(); i++){
                JSONObject date = jsonDates.getJSONObject(i);
                String dateVal = date.getString("date");
                dates.add(dateVal);
                ArrayList<Meal> newMealList = new ArrayList<Meal>();
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
            System.out.println("THIS SORT OF CRASH OCCURED");
            e.printStackTrace();
        }
    }

    public void readFiles(){
        FileInputStream fis = null;
        try{
             fis = getBaseContext().openFileInput("data.txt");
        }
        catch(Exception e){
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

    public static ArrayList<Meal> getMealsAtCurr(){
        return meals.get(currDate);
    }

    public static void changeCurrDate(int inc){
        int newDate = MainActivity.date.getDate() + inc;
        date.setDate(newDate);
        currDate = formatter.format(date);
    }
}