package com.group10.scheduler.domain;

import com.group10.scheduler.domain.strategy.PaymentStrategy;

public class Payment {
    private String paymentId;
    private String bookingId; // FK to Booking
    private double amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private String paymentDate;
    private PaymentStrategy strategy;

    public Payment(String paymentId, String bookingId, double amount, PaymentMethod method,
                    PaymentStatus status, String paymentDate, PaymentStrategy strategy) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.paymentDate = paymentDate;
        this.strategy = strategy;
    }

    public void setStrategy(PaymentStrategy strategy) { this.strategy = strategy; }

    public boolean processDeposit(double amount) {
        boolean ok = strategy.pay(amount);
        this.status = ok ? PaymentStatus.PAID : PaymentStatus.FAILED;
        return ok;
    }

    public boolean processPayment(double amount) {
        boolean ok = strategy.pay(amount);
        this.status = ok ? PaymentStatus.PAID : PaymentStatus.FAILED;
        return ok;
    }

    public boolean refundPayment() {
        boolean ok = strategy.refund(this.amount);
        if (ok) this.status = PaymentStatus.REFUNDED;
        return ok;
    }

    public String getPaymentId() { return paymentId; }
    public String getBookingId() { return bookingId; }
    public double getAmount() { return amount; }
    public PaymentMethod getMethod() { return method; }
    public PaymentStatus getStatus() { return status; }
    public String getPaymentDate() { return paymentDate; }
}
