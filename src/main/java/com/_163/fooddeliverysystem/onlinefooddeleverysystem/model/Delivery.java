package com._163.fooddeliverysystem.onlinefooddeleverysystem.model;

public class Delivery {
    private String deliveryId;
    private String orderId;
    private String deliveryType;
    private String status;

    public Delivery() {}

    public Delivery(String deliveryId, String orderId, String deliveryType, String status) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.deliveryType = deliveryType;
        this.status = status;
    }

    public String getDeliveryId() { return deliveryId; }
    public void setDeliveryId(String deliveryId) { this.deliveryId = deliveryId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getDeliveryType() { return deliveryType; }
    public void setDeliveryType(String deliveryType) { this.deliveryType = deliveryType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String toDataString() {
        return String.join(",", deliveryId, orderId, deliveryType, status);
    }

    public static Delivery fromDataString(String line) {
        String[] tokens = line.split(",", -1);
        if (tokens.length < 4) return null;
        return new Delivery(tokens[0], tokens[1], tokens[2], tokens[3]);
    }

    @Override
    public String toString() {
        return "Delivery{" + "deliveryId='" + deliveryId + '\'' + ", orderId='" + orderId + '\'' + ", deliveryType='" + deliveryType + '\'' + ", status='" + status + '\'' + '}';
    }
}
