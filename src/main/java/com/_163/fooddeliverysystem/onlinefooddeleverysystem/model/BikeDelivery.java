package com._163.fooddeliverysystem.onlinefooddeleverysystem.model;

public class BikeDelivery extends Delivery {
    public BikeDelivery() {
        super();
    }

    public BikeDelivery(String deliveryId, String orderId, String status) {
        super(deliveryId, orderId, "Bike", status);
    }

    public String deliveryMethod() {
        return "Bike delivery - 30 mins typical";
    }
}
