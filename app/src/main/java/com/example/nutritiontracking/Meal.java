package com.example.nutritiontracking;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URI;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Meal implements Serializable {
    ArrayList<String> ingredients = new ArrayList<>();
    Uri uri;
    float cals;
    float carbs;
    float proteins;
    float fats;

    public Meal(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        //empty required constructor
    }

    public Meal(JSONObject jsonObject){
        Uri.decode(jsonObject.optString("uri"));
        cals = Float.parseFloat(jsonObject.optString("cals"));
        carbs = Float.parseFloat(jsonObject.optString("carbs"));
        proteins = Float.parseFloat(jsonObject.optString("proteins"));
        fats = Float.parseFloat(jsonObject.optString("fats"));
        JSONArray jsonIngredients = jsonObject.optJSONArray("ingredients");
        for (int i = 0; i < jsonIngredients.length(); i++){
            String ingredient = (String) jsonIngredients.opt(i);
            ingredients.add(ingredient);
        }
    }

    public void addIngredient(Ingredient ingredient){
        ingredients.add(ingredient.name.split(" -")[0]);
        cals += ingredient.cals;
        carbs += ingredient.carbs;
        fats += ingredient.fats;
        proteins += ingredient.proteins;
    }

    public void setPhotoURI(Uri uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public String getNutrients(){
        return "Calories: " + cals + " calories" + "\n" +
                "Carbs: " + carbs + " grams" + "\n" +
                "Fats: " + fats + " grams" +"\n" +
                "Proteins: " + proteins + " grams";
    }

    public String getIngredients() {
        String temp = "";
        for (String i : ingredients) {
            temp += i + "\n";
        }
        return temp;
    }

    @Override
    public String toString() {
        return "{" +
                "\"uri\":" + "\"" + uri + "\"" +"," +
                "\"ingredients\":[" + String.join(",", ingredientsWithQuotes()) + "]," +
                "\"cals\":" + "\"" + cals + "\"" + "," +
                "\"carbs\":" + "\"" + carbs+ "\"" + "," +
                "\"proteins\":" + "\"" + proteins + "\"" +"," +
                "\"fats\":" + "\"" +  fats + "\"" +
                "}";
    }

    public ArrayList<String> ingredientsWithQuotes(){
        ArrayList<String> newArray = new ArrayList<>();
        for (String ingredient : ingredients){
            newArray.add("\""+ingredient+"\"");
        }
        return newArray;
    }


}
