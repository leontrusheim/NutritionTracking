package com.example.nutritiontracking;

import android.net.Uri;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Meal {
    Date date;
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    Uri uri;
    float cals;
    float carbs;
    float proteins;
    float fats;

    public Meal(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.date = date;
        //empty required constructor
    }

    public void addIngredient(Ingredient ingredient){
        ingredients.add(ingredient);
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

    @Override
    public String toString() {
        return "com.example.nutritiontracking.Meal{" +
                "date=" + date +
                ", ingredients=" + ingredients +
                ", photoURI=" + uri +
                ", cals=" + cals +
                ", carbs=" + carbs +
                ", proteins=" + proteins +
                ", fats=" + fats +
                '}';
    }

    public String getNutrients(){
        return "Calories: " + cals + " calories" + "\n" +
                "Carbs: " + carbs + " grams" + "\n" +
                "Fats: " + fats + " grams" +"\n" +
                "Proteins: " + proteins + " grams";
    }

    public String getIngredients() {
        String temp = "";
        for (Ingredient i : ingredients) {
            temp += i.getName().split("-")[0] + "\n";
        }
        return temp;
    }

}
