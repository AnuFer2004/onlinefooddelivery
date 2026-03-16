package com._163.fooddeliverysystem.onlinefooddeleverysystem.model;

/**
 * FoodItem represents a single item on the restaurant menu.
 *
 * Each food item is stored as one line in "foods.txt" using this format:
 *   foodId,name,price,description
 * Example:
 *   F-1,Veg Burger,5.99,Main Course
 */
public class FoodItem {

    private String foodId;       // unique ID, e.g. "F-1"
    private String name;         // display name, e.g. "Veg Burger"
    private double price;        // price in dollars, e.g. 5.99
    private String description;  // used as category, e.g. "Main Course"

    // Default no-arg constructor (needed for JSON deserialization by Spring)
    public FoodItem() {}

    // Constructor to create a food item with all fields set
    public FoodItem(String foodId, String name, double price, String description) {
        this.foodId = foodId;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────
    // Spring uses these to serialize/deserialize JSON automatically

    public String getFoodId()              { return foodId; }
    public void setFoodId(String foodId)   { this.foodId = foodId; }

    public String getName()                { return name; }
    public void setName(String name)       { this.name = name; }

    public double getPrice()               { return price; }
    public void setPrice(double price)     { this.price = price; }

    public String getDescription()         { return description; }
    public void setDescription(String d)   { this.description = d; }

    /**
     * Returns a human-readable display string for this food item.
     * Used internally for logging.
     */
    public String displayFood() {
        return String.format("%s: %s - %.2f (%s)", foodId, name, price, description);
    }

    /**
     * Converts this food item to a CSV line for file storage.
     * Format: "F-1,Veg Burger,5.99,Main Course"
     */
    public String toDataString() {
        return String.join(",", foodId, name, String.valueOf(price), description);
    }

    /**
     * Parses a CSV line from the file back into a FoodItem object.
     * Returns null if the line is malformed (missing fields).
     *
     * @param line  a line from foods.txt, e.g. "F-1,Veg Burger,5.99,Main Course"
     */
    public static FoodItem fromDataString(String line) {
        // Use limit=-1 to preserve trailing empty values in CSV lines like "F-1,Burger,5.99,".
        String[] tokens = line.split(",", -1);
        if (tokens.length < 3) return null;   // skip bad lines
        try {
            String id = tokens[0];
            String name = tokens[1];
            double price = Double.parseDouble(tokens[2]);
            String description = tokens.length >= 4 ? tokens[3] : "";
            return new FoodItem(id, name, price, description);
        } catch (NumberFormatException e) {
            return null;  // skip lines with invalid price
        }
    }

    @Override
    public String toString() {
        return "FoodItem{id='" + foodId + "', name='" + name + "', price=" + price + "}";
    }
}
