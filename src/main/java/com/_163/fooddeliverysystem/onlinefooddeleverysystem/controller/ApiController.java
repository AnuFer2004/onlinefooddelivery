package com._163.fooddeliverysystem.onlinefooddeleverysystem.controller;

import com._163.fooddeliverysystem.onlinefooddeleverysystem.model.FoodItem;
import com._163.fooddeliverysystem.onlinefooddeleverysystem.model.Order;
import com._163.fooddeliverysystem.onlinefooddeleverysystem.model.Delivery;
import com._163.fooddeliverysystem.onlinefooddeleverysystem.model.Payment;
import com._163.fooddeliverysystem.onlinefooddeleverysystem.model.User;
import com._163.fooddeliverysystem.onlinefooddeleverysystem.service.DeliveryService;
import com._163.fooddeliverysystem.onlinefooddeleverysystem.service.FoodService;
import com._163.fooddeliverysystem.onlinefooddeleverysystem.service.OrderService;
import com._163.fooddeliverysystem.onlinefooddeleverysystem.service.PaymentService;
import com._163.fooddeliverysystem.onlinefooddeleverysystem.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final UserService userService = new UserService();
    private final FoodService foodService = new FoodService();
    private final OrderService orderService = new OrderService();
    private final DeliveryService deliveryService = new DeliveryService();
    private final PaymentService paymentService = new PaymentService();

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("ok");
    }

    // =========== FOOD ENDPOINTS ===========
    @GetMapping("/foods")
    public List<Map<String, Object>> getFoods() {
        return foodService.getAllFoods().stream().map(this::foodToMap).collect(Collectors.toList());
    }

    @PostMapping("/foods")
    public ResponseEntity<String> addFood(@RequestBody Map<String, Object> payload) {
        String name = Objects.toString(payload.get("name"), "").trim();
        String description = Objects.toString(payload.get("description"), "").trim();
        String foodId = Objects.toString(payload.get("foodId"), "").trim();
        double price;

        try {
            price = Double.parseDouble(payload.getOrDefault("price", "0").toString());
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body("Invalid price");
        }

        if (name.isEmpty() || price <= 0) {
            return ResponseEntity.badRequest().body("Invalid food name or price");
        }

        if (foodId.isEmpty()) {
            foodId = generateNextFoodId();
        }

        if (foodService.getFoodById(foodId) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Food ID already exists");
        }

        FoodItem item = new FoodItem(foodId, name, price, description);
        foodService.addFood(item);
        return ResponseEntity.status(HttpStatus.CREATED).body("Food added");
    }

    @DeleteMapping("/foods/{foodId}")
    public ResponseEntity<String> deleteFood(@PathVariable String foodId) {
        if (foodId == null || foodId.isBlank()) {
            return ResponseEntity.badRequest().body("Food ID is required");
        }
        boolean removed = foodService.deleteFood(foodId);
        if (!removed) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food item not found");
        }
        return ResponseEntity.ok("Food item removed");
    }

    // =========== ORDER ENDPOINTS ===========
    @GetMapping("/orders")
    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/user/{username}")
    public List<Order> getOrdersForUser(@PathVariable String username) {
        if (username == null || username.isBlank()) {
            return Collections.emptyList();
        }
        return orderService.getAllOrders().stream()
                .filter(o -> o.getCustomerName() != null && o.getCustomerName().equalsIgnoreCase(username))
                .collect(Collectors.toList());
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable String orderId, @RequestBody Map<String, String> payload) {
        String status = Objects.toString(payload.get("status"), "").trim();
        if (orderId == null || orderId.isBlank() || status.isBlank()) {
            return ResponseEntity.badRequest().body("orderId and status are required");
        }

        if (!orderService.updateOrderStatus(orderId, status)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }
        return ResponseEntity.ok("Order status updated");
    }

    // =========== DELIVERY ENDPOINTS ===========
    @GetMapping("/deliveries")
    public List<Delivery> getDeliveries() {
        return deliveryService.getAll();
    }

    @PostMapping("/deliveries")
    public ResponseEntity<String> createDelivery(@RequestBody Map<String, Object> payload) {
        String orderId = Objects.toString(payload.get("orderId"), "").trim();
        String deliveryType = Objects.toString(payload.get("deliveryType"), "Bike").trim();
        String status = Objects.toString(payload.get("status"), "Assigned").trim();
        String deliveryId = Objects.toString(payload.get("deliveryId"), "").trim();

        if (orderId.isEmpty()) {
            return ResponseEntity.badRequest().body("orderId required");
        }
        if (deliveryId.isEmpty()) {
            deliveryId = generateNextDeliveryId();
        }
        if (deliveryService.getById(deliveryId) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Delivery ID already exists");
        }
        Delivery d = new Delivery(deliveryId, orderId, deliveryType, status);
        deliveryService.assignDelivery(d);
        return ResponseEntity.status(HttpStatus.CREATED).body("Delivery assigned");
    }

    @PutMapping("/deliveries/{deliveryId}")
    public ResponseEntity<String> updateDeliveryStatus(@PathVariable String deliveryId, @RequestBody Map<String, String> payload) {
        String status = Objects.toString(payload.get("status"), "").trim();
        if (status.isEmpty()) {
            return ResponseEntity.badRequest().body("status required");
        }
        boolean updated = deliveryService.updateDeliveryStatus(deliveryId, status);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delivery not found");
        }

        // If delivery is completed, mark order as completed too for consistency
        if ("Delivered".equalsIgnoreCase(status)) {
            Delivery delivery = deliveryService.getById(deliveryId);
            if (delivery != null && delivery.getOrderId() != null) {
                orderService.updateOrderStatus(delivery.getOrderId(), "Completed");
            }
        }

        return ResponseEntity.ok("Delivery status updated");
    }

    @DeleteMapping("/deliveries/{deliveryId}")
    public ResponseEntity<String> deleteDelivery(@PathVariable String deliveryId) {
        if (!deliveryService.removeDelivery(deliveryId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }
        return ResponseEntity.ok("Delivery removed");
    }

    // =========== PAYMENT ENDPOINTS ===========
    @GetMapping("/payments")
    public List<Payment> getPayments() {
        return paymentService.getAll();
    }

    @PostMapping("/payments")
    public ResponseEntity<String> recordPayment(@RequestBody Map<String, Object> payload) {
        String orderId = Objects.toString(payload.get("orderId"), "").trim();
        String method = Objects.toString(payload.get("paymentMethod"), "Cash").trim();
        String status = Objects.toString(payload.get("status"), "Completed").trim();
        String paymentId = Objects.toString(payload.get("paymentId"), "").trim();
        double amount;
        try {
            amount = Double.parseDouble(payload.getOrDefault("amount", "0").toString());
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body("Invalid amount");
        }

        if (orderId.isEmpty() || amount <= 0) {
            return ResponseEntity.badRequest().body("orderId and amount required");
        }
        if (paymentId.isEmpty()) {
            paymentId = generateNextPaymentId();
        }
        if (paymentService.getById(paymentId) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Payment ID already exists");
        }

        Payment p = new Payment(paymentId, orderId, amount, method, status);
        paymentService.recordPayment(p);
        return ResponseEntity.status(HttpStatus.CREATED).body("Payment recorded");
    }

    @PutMapping("/payments/{paymentId}")
    public ResponseEntity<String> updatePayment(@PathVariable String paymentId, @RequestBody Map<String, String> payload) {
        String status = Objects.toString(payload.get("status"), "").trim();
        if (status.isEmpty()) {
            return ResponseEntity.badRequest().body("status required");
        }
        boolean updated = paymentService.updatePaymentStatus(paymentId, status);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
        }

        // If payment completed, mark related order as Paid
        if ("Completed".equalsIgnoreCase(status)) {
            Payment payment = paymentService.getById(paymentId);
            if (payment != null && payment.getOrderId() != null) {
                orderService.updateOrderStatus(payment.getOrderId(), "Paid");
            }
        }

        return ResponseEntity.ok("Payment status updated");
    }

    @DeleteMapping("/payments/{paymentId}")
    public ResponseEntity<String> deletePayment(@PathVariable String paymentId) {
        if (!paymentService.removePayment(paymentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }
        return ResponseEntity.ok("Payment removed");
    }

    @PostMapping("/orders")
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        if (order == null || order.getCustomerName() == null || order.getCustomerName().isBlank()) {
            return ResponseEntity.badRequest().body("Invalid order data");
        }

        if (order.getOrderId() == null || order.getOrderId().isBlank()) {
            order.setOrderId(generateNextOrderId());
        }
        if (order.getStatus() == null || order.getStatus().isBlank()) {
            order.setStatus("Pending");
        }
        if (order.getTotalPrice() <= 0.0) {
            // autopopulate with 0 when not provided or invalid; allows frontend total price to carry through
            order.setTotalPrice(0.0);
        }

        if (!orderService.placeOrder(order)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Order already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Order placed");
    }

    // =========== USER AUTH ENDPOINTS ===========
    @PostMapping("/users/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> payload) {
        String username = Objects.toString(payload.get("username"), "").trim();
        String password = Objects.toString(payload.get("password"), "").trim();

        if (username.isEmpty() || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        User user = userService.loginUser(username, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(userToMap(user));
    }

    @PostMapping("/users/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> payload) {
        String username = Objects.toString(payload.get("username"), "").trim();
        String password = Objects.toString(payload.get("password"), "").trim();
        String email = Objects.toString(payload.get("email"), "").trim();
        String address = Objects.toString(payload.get("phone"), "").trim();

        if (username.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body("username/password required");
        }

        if (userService.getAllUsers().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken");
        }

        String userId = generateNextUserId();
        User newUser = new User(userId, username, email, password, address);
        boolean added = userService.registerUser(newUser);

        if (!added) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not register");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Registered");
    }

    @GetMapping("/admin/users")
    public List<Map<String, Object>> listUsers() {
        return userService.getAllUsers().stream().map(this::userToMap).collect(Collectors.toList());
    }

    // =========== HELPERS ===========
    private Map<String, Object> foodToMap(FoodItem f) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", f.getFoodId());
        m.put("name", f.getName());
        m.put("price", f.getPrice());
        return m;
    }

    private Map<String, Object> userToMap(User u) {
        Map<String, Object> m = new HashMap<>();
        m.put("userId", u.getUserId());
        m.put("username", u.getUsername());
        m.put("email", u.getEmail());
        m.put("address", u.getAddress());
        return m;
    }

    private String generateNextUserId() {
        int next = userService.getAllUsers().stream()
                .mapToInt(u -> {
                    String id = u.getUserId();
                    if (id != null && id.startsWith("U-")) {
                        try { return Integer.parseInt(id.substring(2)); } catch (NumberFormatException e) { }
                    }
                    return 0;
                })
                .max().orElse(1000) + 1;
        return "U-" + next;
    }

    private String generateNextFoodId() {
        int next = foodService.getAllFoods().stream()
                .mapToInt(f -> {
                    String id = f.getFoodId();
                    if (id != null && id.startsWith("F-")) {
                        try { return Integer.parseInt(id.substring(2)); } catch (NumberFormatException e) { }
                    }
                    return 0;
                })
                .max().orElse(0) + 1;
        return "F-" + next;
    }

    private String generateNextOrderId() {
        int next = orderService.getAllOrders().stream()
                .mapToInt(o -> {
                    String id = o.getOrderId();
                    if (id != null && id.startsWith("ORD-")) {
                        try { return Integer.parseInt(id.substring(4)); } catch (NumberFormatException e) { }
                    }
                    return 0;
                })
                .max().orElse(0) + 1;
        return "ORD-" + next;
    }

    private String generateNextDeliveryId() {
        int next = deliveryService.getAll().stream()
                .mapToInt(d -> {
                    String id = d.getDeliveryId();
                    if (id != null && id.startsWith("D-")) {
                        try { return Integer.parseInt(id.substring(2)); } catch (NumberFormatException e) { }
                    }
                    return 0;
                })
                .max().orElse(0) + 1;
        return "D-" + next;
    }

    private String generateNextPaymentId() {
        int next = paymentService.getAll().stream()
                .mapToInt(p -> {
                    String id = p.getPaymentId();
                    if (id != null && id.startsWith("P-")) {
                        try { return Integer.parseInt(id.substring(2)); } catch (NumberFormatException e) { }
                    }
                    return 0;
                })
                .max().orElse(0) + 1;
        return "P-" + next;
    }
}
