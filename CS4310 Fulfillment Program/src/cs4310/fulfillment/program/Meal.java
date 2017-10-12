/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jacobh
 * This class is to keep a meal item and all of the information about it.
 */
public class Meal {
    private int price;
    private List<String> ingredients = new ArrayList<String>();
    private String name;
    private int cookTime;
    private String description;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List getAllIngredients() {
        return ingredients;
    }
    
    public String getIngredient(int location){
        return ingredients.get(location);
    }

    public void addIngredien(String ingredient) {
        ingredients.add(ingredient);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
    
}
