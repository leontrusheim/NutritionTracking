package com.example.nutritiontracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public  class MainActivity extends AppCompatActivity {

    public String example = "{\"dates\":[{\"date\":\"04/27/2022\",\"meals\":[{\"uri\":\"null\",\"ingredients\":[],\"cals\":\"0.0\",\"carbs\":\"0.0\",\"proteins\":\"0.0\",\"fats\":\"0.0\"},{\"uri\":\"null\",\"ingredients\":[\"Mixed Lettuce\"],\"cals\":\"19.98\",\"carbs\":\"4.21\",\"proteins\":\"1.23\",\"fats\":\"0.2\"},{\"uri\":\"content://com.rypittner.android.fileprovider/my_images/JPEG__9006168466491347100.jpg\",\"ingredients\":[],\"cals\":\"0.0\",\"carbs\":\"0.0\",\"proteins\":\"0.0\",\"fats\":\"0.0\"}]}]}";
    public static String searchTerm;

    public static ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
    public static HashMap<String, ArrayList<Meal>> meals = new HashMap<>();

    public static Date date = new Date();

    public static ArrayList<String> dates = new ArrayList<String>();

    public static SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    public static String currDate = formatter.format(date);
    public static Meal currMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stringToMeals(example);

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
        Fragment progressFrag = new ProgressSelectorFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
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

    public void reloadAddMealPage(){
        Fragment addMealFrag = new AddMealFragment(meals.get(currDate), currDate);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, addMealFrag);
        fragmentTransaction.commit();
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

    public void onClickChangeDate(View v){
        if (v.getId() == R.id.tomorrow){
            int newDate = date.getDate() + 1;
            date.setDate(newDate);
        }
        else{
            int newDate = date.getDate() - 1;
            date.setDate(newDate);
        }
        currDate = formatter.format(date);
        reloadAddMealPage();
    }

    public void saveToFile(){
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
        System.out.println(temp);
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
        catch(Exception e){e.printStackTrace();}
    }
}