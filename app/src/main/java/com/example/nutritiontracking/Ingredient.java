/*
 * @authors: Ryan Pittner & Leon Trusheim
 * @file: Ingredient.java
 * @assignment: Nutrition Tracking (Final Project)
 * @course: CSc 317 - Spring 2022 (Dicken)
 * @description: This class represents an Ingredient object, which is a custom class
 * object. Each ingredient has a certain name, brand, and float values for the calories
 * and 3 nutrients (carbs, proteins, fats). An ingredient can be constructed by passing
 * the class a jsonObject for that ingredient.
 */
package com.example.nutritiontracking;

import org.json.JSONObject;

import java.io.Serializable;

import java.text.DecimalFormat;

public class Ingredient implements Serializable {

    String name, brand;
    float cals, carbs, proteins, fats;

    /**
     * Constructor for ingredient object. Takes a jsonObject and parses each
     * of its fields into the class.
     * @param jsonObject
     */
    public Ingredient(JSONObject jsonObject) {
        name = jsonObject.optString("item_name");
        brand = jsonObject.optString("brand_name");
        carbs = parseFloat(jsonObject.optString("nf_total_carbohydrate"));
        fats =  parseFloat(jsonObject.optString("nf_total_fat"));
        proteins =  parseFloat(jsonObject.optString("nf_protein"));
        DecimalFormat df_obj = new DecimalFormat("#.##");
        cals = Float.parseFloat(df_obj.format(carbs * 4 + fats * 9 + proteins *4));
    }

    /**
     * Returns a float from a string input. Returns 0 if it fails to
     * parse
     */
    public float parseFloat(String s){
        if (s.equals("null") || s == null){
            return 0;}
        else {
            return Float.parseFloat(s);
        }
    }

    /**
     * Returns a the name of the ingredient as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a the brand of the ingredient as a string
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Returns a string summary of the nutrients for the ingredients
     */
    public String getNutrients(){
        return "Calories: " + cals + " calories" + "\n" +
                "Carbs: " + carbs + " grams" + "\n" +
                "Fats: " + fats + " grams" +"\n" +
                "Proteins: " + proteins + " grams";
    }
}
