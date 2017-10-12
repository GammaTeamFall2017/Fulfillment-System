/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4310.fulfillment.program;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author jacobh
 * This class is to keep all of the meals in one location and to be able to access them.
 */
public class Cookbook {
    Set<Meal> meals = new HashSet<Meal>();
    
    public boolean add(Meal newMeal){
        if(!meals.add(newMeal))
            return false;
        else
            return true;
    }
            
    public boolean remove(Meal removeMeal){
        
        if(meals.contains(removeMeal)){
            if(meals.remove(removeMeal))
                return true;
            else 
                return false;
        }
        else
            return false;
    }
            
            
    public Meal getMeal(String mealName){
        for(Meal looker: meals){
            if(looker.getName().equals(mealName))
                return looker;
        }
        return null;
        
    }
    
    public ArrayList getAllMeals(){
        ArrayList tempArray = new ArrayList();
        for(Meal setter : meals)
            tempArray.add(setter);
        
        return tempArray;
    }
    
}
