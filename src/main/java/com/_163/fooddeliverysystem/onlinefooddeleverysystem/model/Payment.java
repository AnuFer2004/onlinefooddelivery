package com._163.fooddeliverysystem.onlinefooddeleverysystem.model;

public class Payment {
    private String paymentId;
    private String orderId;
    private double amount;
    private String paymentMethod;
    private String status;

    public Payment() {}

    public Payment(String paymentId, String orderId, double amount, String paymentMethod, String status) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String toDataString() {
        return String.join(",", paymentId, orderId, String.valueOf(amount), paymentMethod, status);
    }

    public static Payment fromDataString(String line) {
        String[] tokens = line.split(",", -1);
        if (tokens.length < 5) return null;
        try {
            return new Payment(tokens[0], tokens[1], Double.parseDouble(tokens[2]), tokens[3], tokens[4]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Payment{" + "paymentId='" + paymentId + '\'' + ", orderId='" + orderId + '\'' + ", amount=" + amount + ", paymentMethod='" + paymentMethod + '\'' + ", status='" + status + '\'' + '}';
    }
}
