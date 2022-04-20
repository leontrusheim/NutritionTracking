import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

public class Meal {
    Date date;
    ArrayList<String> ingredients;
    URI photoURI;
    int cals;
    int carbs;
    int proteins;
    int fats;

    public Meal(Date date, URI photoURI){
        this.date = date;
        this.photoURI = photoURI;
        cals = 0;
        carbs = 0;
        proteins = 0;
        fats = 0;
    }

    public int getCals() {
        return cals;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getFats() {
        return fats;
    }

    public int getProteins() {
        return proteins;
    }

    public void setCals(int cals) {
        this.cals = cals;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public void addIngredient(String ingredient){
        ingredients.add(ingredient);
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "date=" + date +
                ", ingredients=" + ingredients +
                ", photoURI=" + photoURI +
                ", cals=" + cals +
                ", carbs=" + carbs +
                ", proteins=" + proteins +
                ", fats=" + fats +
                '}';
    }
}
