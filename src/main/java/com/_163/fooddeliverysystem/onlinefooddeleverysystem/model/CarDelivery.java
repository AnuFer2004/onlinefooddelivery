package com._163.fooddeliverysystem.onlinefooddeleverysystem.model;

public class CarDelivery extends Delivery {
    public CarDelivery() {
        super();
    }

    public CarDelivery(String deliveryId, String orderId, String status) {
        super(deliveryId, orderId, "Car", status);
    }

    public String deliveryMethod() {
        return "Car delivery (suitable for bulk/long distance)";
    }
}
