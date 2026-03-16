package com._163.fooddeliverysystem.onlinefooddeleverysystem.service;

import com._163.fooddeliverysystem.onlinefooddeleverysystem.model.Delivery;

public class DeliveryService extends BaseCsvService<Delivery> {
    private static final String DELIVERY_FILE = "deliveries.txt";

    @Override
    protected String getFileName() {
        return DELIVERY_FILE;
    }

    @Override
    protected Delivery fromDataString(String line) {
        return Delivery.fromDataString(line);
    }

    @Override
    protected String toDataString(Delivery item) {
        return item.toDataString();
    }

    @Override
    protected String getId(Delivery item) {
        return item == null ? null : item.getDeliveryId();
    }

    public boolean assignDelivery(Delivery delivery) {
        return add(delivery);
    }

    public boolean updateDeliveryStatus(String deliveryId, String status) {
        Delivery d = getById(deliveryId);
        if (d == null) return false;
        d.setStatus(status);
        return update(d);
    }

    public boolean removeDelivery(String deliveryId) {
        return delete(deliveryId);
    }

    public Delivery trackDelivery(String deliveryId) {
        return getById(deliveryId);
    }
}
