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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public  class MainActivity extends AppCompatActivity {
    public static String foodName;
    public static ArrayList<String[]> items = new ArrayList<String[]>();;
    public static HashMap<String[],String[]> nutrients = new HashMap<String[],String[]>();;


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
        Fragment mealSummaryFragment = new MealSummaryFragment();
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
        EditText input = findViewById(R.id.edit_food);
        foodName = String.valueOf(input.getText());
        new SearchTask().execute();
    }

    public void setFoodSearchFragment(){
        FoodSearchFragment searchFragment = new FoodSearchFragment();
        searchFragment.setContainerActivity(this);
        Bundle args = new Bundle();
        args.putSerializable("items", items);
        args.putSerializable("nutrients", nutrients);
        args.putString("foodName", foodName);
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
        protected void onPostExecute(JSONObject tiobe) {
            try {
                items = new ArrayList<String[]>();
                nutrients = new HashMap<String[],String[]>();
                JSONArray infoArray = tiobe.getJSONArray("hits");
                for (int i = 0; i < infoArray.length(); i++){
                    JSONObject infos = infoArray.getJSONObject(i);
                    JSONObject info = infos.getJSONObject("fields");
                    String itemName = info.optString("item_name");
                    String brandName = info.optString("brand_name");
                    String calories = info.optString("nf_calories");
                    String carbs = info.optString("nf_total_carbohydrate");
                    String fat = info.optString("nf_total_fat");
                    String protein = info.optString("nf_protein");
                    String[] key = new String[]{itemName,brandName};
                    String[] values = new String[]{calories,carbs,fat,protein};
                    items.add(key);
                    nutrients.put(key,values);
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
                    foodName + "?results=0:50&fields=item_name,brand_name,nf_calories,nf_total_fat" +
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
        String text = (String)((TextView)v).getText();
        NutrientFragment nutrientFragment = new NutrientFragment();
        nutrientFragment.setContainerActivity(this);
        Bundle args = new Bundle();
        String[] nutrients = (String[]) ((TextView)v).getTag();
        args.putStringArray("nutrients",nutrients);
        args.putString("info",text);
        nutrientFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.mainContent, nutrientFragment);
        fragmentTransaction.commit();
    }

    public void onClickCameraIntent(View v){

    }


}