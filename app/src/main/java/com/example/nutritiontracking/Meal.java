/*
 * @authors: Ryan Pittner & Leon Trusheim
 * @file: Meal.java
 * @assignment: Nutrition Tracking (Final Project)
 * @course: CSc 317 - Spring 2022 (Dicken)
 * @description: This represents a custom Meal class object. Each Meal object has
 *      number of Ingredient objects, a bitmap image stored as a string, and a float value
 *      for the calories of the meal and the three nutritional values (carbs, fats, proteins).
 *      There is also a constructor that gets passed a jsonObject for the meal that will construct
 *      the meal.
 */

package com.example.nutritiontracking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import java.util.ArrayList;

public class Meal implements Serializable {
    ArrayList<String> ingredients = new ArrayList<>();
    String bitmap;
    float cals, carbs, proteins, fats;

    public Meal(){
        //empty required constructor
    }

    /**
     * Constructor for the meal
     * @param jsonObject -- a json object for the meal
     */

    public Meal(JSONObject jsonObject){
        bitmap = jsonObject.optString("bitmap");
        Uri.parse(jsonObject.optString("uri"));
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

    /**
     * Adds an ingredient to the meal. Automatically updates the calories, carbs,
     * fats, and protiens for the meal based on that of the added ingredient.
     * @param ingredient -- the ingredient to be added to the meal
     */
    public void addIngredient(Ingredient ingredient){
        ingredients.add(ingredient.name.split(" -")[0]);
        cals += ingredient.cals;
        carbs += ingredient.carbs;
        fats += ingredient.fats;
        proteins += ingredient.proteins;
    }


    /**
     * Returns a string representation of the nutrients of the meal
     */
    public String getNutrients(){
        return "Calories: " + cals + " calories" + "\n" +
                "Carbs: " + carbs + " grams" + "\n" +
                "Fats: " + fats + " grams" +"\n" +
                "Proteins: " + proteins + " grams";
    }

    /**
     * Returns a string of all ingredients in the meal
     */
    public String getIngredients() {
        String temp = "";
        for (String i : ingredients) {
            temp += " - " + i + "\n";
        }
        return temp;
    }

    /**
     * Returns a string representation of the meal (formatted as a json object)
     */
    @Override
    public String toString() {
        return "{" +
                "\"bitmap\":" + "\"" + bitmap + "\"" +"," +
                "\"ingredients\":[" + String.join(",", ingredientsWithQuotes()) + "]," +
                "\"cals\":" + "\"" + cals + "\"" + "," +
                "\"carbs\":" + "\"" + carbs+ "\"" + "," +
                "\"proteins\":" + "\"" + proteins + "\"" +"," +
                "\"fats\":" + "\"" +  fats + "\"" +
                "}";
    }

    /**
     * Setter for the bitmap string. Takes a bitmap and converts it to a string, then sets the
     * bitmapStr field in the class to it.
     *
     * @param b -- the bitmap to be converted to a string and saved
     */
    public void setBitmap(Bitmap b) {
        String bitmapStr = bitmapToString(b);
        this.bitmap = bitmapStr;
    }

    /**
     * Returns an array where each ingredient has a quote around the name. This method is used
     * to create a json string object of the meal object.
     */
    private ArrayList<String> ingredientsWithQuotes(){
        ArrayList<String> newArray = new ArrayList<>();
        for (String ingredient : ingredients){
            newArray.add("\""+ingredient+"\"");
        }
        return newArray;
    }

    /**
     * Converts a bitmap to a compressed string representation
     *
     * @param bitmap -- the bitmap to be converted to a string
     */
    private String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,25, baos);
        byte [] b=baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }


    /**
     * Returns a bitmap representation of the bitmap string, or null if it fails to convert.
     */
    public Bitmap getBitmap(){
        String encodedString = bitmap;
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


}
