package com._163.fooddeliverysystem.onlinefooddeleverysystem.service;

import com._163.fooddeliverysystem.onlinefooddeleverysystem.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderService extends BaseCsvService<Order> {
    private static final String ORDER_FILE = "orders.txt";

    @Override
    protected String getFileName() {
        return ORDER_FILE;
    }

    @Override
    protected Order fromDataString(String line) {
        return Order.fromDataString(line);
    }

    @Override
    protected String toDataString(Order item) {
        return item.toDataString();
    }

    @Override
    protected String getId(Order item) {
        return item == null ? null : item.getOrderId();
    }

    public boolean placeOrder(Order order) {
        if (order == null || order.getOrderId() == null || order.getOrderId().isBlank()) {
            return false;
        }
        return add(order);
    }

    public List<Order> viewOrders() {
        return getAll();
    }

    public boolean updateOrderStatus(String orderId, String status) {
        Order order = getById(orderId);
        if (order == null) {
            return false;
        }
        order.setStatus(status);
        return update(order);
    }

    public boolean cancelOrder(String orderId) {
        return updateOrderStatus(orderId, "Canceled");
    }

    public Order getOrderById(String orderId) {
        return getById(orderId);
    }

    public List<Order> getAllOrders() {
        return getAll();
    }

    public List<String> viewOrderSummaries() {
        List<String> result = new ArrayList<>();
        for (Order order : getAll()) {
            result.add(order.toString());
        }
        return result;
    }
}

