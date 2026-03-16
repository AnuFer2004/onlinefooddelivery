package com._163.fooddeliverysystem.onlinefooddeleverysystem.model;

public class CardPayment extends Payment {

    public CardPayment() {
        super();
    }

    public CardPayment(String paymentId, String orderId, double amount, String status) {
        super(paymentId, orderId, amount, "Card", status);
    }

    public String paymentType() {
        return "Card";
    }
}
