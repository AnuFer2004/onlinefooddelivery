package com._163.fooddeliverysystem.onlinefooddeleverysystem.model;

public class CashPayment extends Payment {

    public CashPayment() {
        super();
    }

    public CashPayment(String paymentId, String orderId, double amount, String status) {
        super(paymentId, orderId, amount, "Cash", status);
    }

    public String paymentType() {
        return "Cash";
    }
}
