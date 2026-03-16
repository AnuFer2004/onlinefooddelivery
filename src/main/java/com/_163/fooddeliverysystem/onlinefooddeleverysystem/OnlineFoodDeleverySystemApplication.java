package com._163.fooddeliverysystem.onlinefooddeleverysystem;

import com._163.fooddeliverysystem.onlinefooddeleverysystem.model.FoodItem;
import com._163.fooddeliverysystem.onlinefooddeleverysystem.model.User;
import com._163.fooddeliverysystem.onlinefooddeleverysystem.service.FoodService;
import com._163.fooddeliverysystem.onlinefooddeleverysystem.service.OrderService;
import com._163.fooddeliverysystem.onlinefooddeleverysystem.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineFoodDeleverySystemApplication implements CommandLineRunner {
    private final UserService userService = new UserService();
    private final FoodService foodService = new FoodService();
    private final OrderService orderService = new OrderService();

    public static void main(String[] args) {
        SpringApplication.run(OnlineFoodDeleverySystemApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (userService.getAllUsers().isEmpty()) {
            userService.registerUser(new User("A-1", "admin", "admin@example.com", "admin", "HQ"));
        }
        if (foodService.getAllFoods().isEmpty()) {
            foodService.addFood(new FoodItem("F-1", "Veg Burger", 5.99, "Delicious vegetal burger"));
            foodService.addFood(new FoodItem("F-2", "Chicken Wings", 8.99, "Spicy chicken wings"));
        }
    }
}


