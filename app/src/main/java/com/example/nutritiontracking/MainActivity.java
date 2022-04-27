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
    public static String searchTerm;
    public static ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
    public static HashMap<String, ArrayList<Meal>> meals = new HashMap<>();
    public static String currDate;
    public static Meal currMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Fragment mealFrag = new AddMealFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, mealFrag);
        fragmentTransaction.commit();
    }

    /**
     * Creates a fragment to display the meal summary and replaces the UI to display it.
     */
    public void onClickMealSummary(View v){
        Fragment mealSummaryFragment = new MealSummaryFragment(meals.get(currDate).get(0));
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
        Fragment addMealFrag = new AddMealFragment(meals.get(currDate));
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, addMealFrag);
        fragmentTransaction.commit();
    }

    public void onClickAddMeal(View v){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String dateStr =  formatter.format(date);
        System.out.println("DATE STRING" + dateStr);
        currDate = dateStr;
        System.out.println("CURRENT" + currDate);
        Meal newMeal = new Meal();
        currMeal = newMeal;
        ArrayList<Meal> dateMeals;
        if (meals.get(dateStr) == null){
            System.out.println("MEALS ARE NULL");
            dateMeals = new ArrayList<Meal>();
            dateMeals.add(0, newMeal);
        }
        else{
            dateMeals = meals.get(dateStr);
            dateMeals.add(0, newMeal);
        }
        meals.put(dateStr, dateMeals);

        Fragment photoFrag = new AddPhotoFragment(currMeal);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, photoFrag);
        fragmentTransaction.commit();
    }

    public void setFoodSearchFragment(){
        FoodSearchFragment searchFragment = new FoodSearchFragment(currMeal);
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

        NutrientFragment nutrientFragment = new NutrientFragment(currMeal);
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
}