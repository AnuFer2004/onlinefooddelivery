package com._163.fooddeliverysystem.onlinefooddeleverysystem.service;

import com._163.fooddeliverysystem.onlinefooddeleverysystem.model.Payment;

public class PaymentService extends BaseCsvService<Payment> {
    private static final String PAYMENT_FILE = "payments.txt";

    @Override
    protected String getFileName() {
        return PAYMENT_FILE;
    }

    @Override
    protected Payment fromDataString(String line) {
        return Payment.fromDataString(line);
    }

    @Override
    protected String toDataString(Payment item) {
        return item.toDataString();
    }

    @Override
    protected String getId(Payment item) {
        return item == null ? null : item.getPaymentId();
    }

    public boolean recordPayment(Payment payment) {
        return add(payment);
    }

    public boolean updatePaymentStatus(String paymentId, String status) {
        Payment p = getById(paymentId);
        if (p == null) return false;
        p.setStatus(status);
        return update(p);
    }

    public boolean removePayment(String paymentId) {
        return delete(paymentId);
    }
}
