package application.Controller.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FoodContainer {
    private static ObservableList<Food> foods = FXCollections.observableArrayList();

    public static ObservableList<Food> getFoods() {
        return foods;
    }

    public static void addFood(Food food) {
        foods.add(food);
    }

    public static void removeFood(Food food){
        foods.remove(food);
    }

    public static Food searchByCode(String string) {
        for (Food food:foods){
            if (food.getCode().equals(string))
                return food;
        }
        return null;
    }
}
