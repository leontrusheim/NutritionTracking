package com.example.nutritiontracking;

import org.json.JSONObject;

import java.io.Serializable;

public class Ingredient implements Serializable {
    String name;
    String brand;
    float cals;
    float carbs;
    float proteins;
    float fats;

    public Ingredient(JSONObject jsonObject) {

        name = jsonObject.optString("item_name");
        brand = jsonObject.optString("brand_name");
        cals = parseFloat(jsonObject.optString("nf_calories"));
        carbs = parseFloat(jsonObject.optString("nf_total_carbohydrate"));
        fats =  parseFloat(jsonObject.optString("nf_total_fat"));
        proteins =  parseFloat(jsonObject.optString("nf_protein"));
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", cals=" + cals +
                ", carbs=" + carbs +
                ", proteins=" + proteins +
                ", fats=" + fats +
                '}';
    }

    public float parseFloat(String s){
        if (s.equals("null") || s == null){
            return 0;}
        else {
            return Float.parseFloat(s);
        }
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getNutrients(){
        return "Calories: " + cals + " calories" + "\n" +
                "Carbs: " + carbs + " grams" + "\n" +
                "Fats: " + fats + " grams" +"\n" +
                "Proteins: " + proteins + " grams";
    }
}
