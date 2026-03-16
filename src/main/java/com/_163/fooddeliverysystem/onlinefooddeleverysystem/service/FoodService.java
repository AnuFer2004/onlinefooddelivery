package com._163.fooddeliverysystem.onlinefooddeleverysystem.service;

import com._163.fooddeliverysystem.onlinefooddeleverysystem.model.FoodItem;
import com._163.fooddeliverysystem.onlinefooddeleverysystem.util.FileManager;

import java.util.ArrayList;
import java.util.List;

public class FoodService extends BaseCsvService<FoodItem> {

    private static final String FOOD_FILE = "foods.txt";

    @Override
    protected String getFileName() {
        return FOOD_FILE;
    }

    @Override
    protected FoodItem fromDataString(String line) {
        return FoodItem.fromDataString(line);
    }

    @Override
    protected String toDataString(FoodItem item) {
        return item.toDataString();
    }

    @Override
    protected String getId(FoodItem item) {
        return item == null ? null : item.getFoodId();
    }

    public boolean addFood(FoodItem food) {
        return add(food);
    }

    public boolean updateFood(FoodItem updated) {
        return update(updated);
    }

    public boolean deleteFood(String foodId) {
        return delete(foodId);
    }

    public FoodItem getFoodById(String foodId) {
        return getById(foodId);
    }

    public List<FoodItem> getAllFoods() {
        return getAll();
    }

    public List<String> viewFoodMenu() {
        List<String> menu = new ArrayList<>();
        for (FoodItem item : getAll()) {
            menu.add(item.displayFood());
        }
        return menu;
    }
}

