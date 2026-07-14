package com.group10.scheduler.domain.strategy;

import com.group10.scheduler.domain.PaymentMethod;

/** Placeholder ConcreteStrategy classes — always succeed. Swap for real logic. */
public class ConcreteStrategies {

    public static class CreditCardStrategy implements PaymentStrategy {
        private String cardNumber, expiryDate, cvv;
        public CreditCardStrategy(String cardNumber, String expiryDate, String cvv) {
            this.cardNumber = cardNumber; this.expiryDate = expiryDate; this.cvv = cvv;
        }
        public boolean pay(double amount) { return true; }
        public boolean refund(double amount) { return true; }
    }

    public static class DebitCardStrategy implements PaymentStrategy {
        private String cardNumber, pin;
        public DebitCardStrategy(String cardNumber, String pin) {
            this.cardNumber = cardNumber; this.pin = pin;
        }
        public boolean pay(double amount) { return true; }
        public boolean refund(double amount) { return true; }
    }

    public static class InstitutionalBillingStrategy implements PaymentStrategy {
        private long organizationId;
        private String billingAccount;
        public InstitutionalBillingStrategy(long organizationId, String billingAccount) {
            this.organizationId = organizationId; this.billingAccount = billingAccount;
        }
        public boolean pay(double amount) { return true; }
        public boolean refund(double amount) { return true; }
    }

    /** Minimal reconstruction from a CSV method string; card/org details would come from user profile in the real system. */
    public static PaymentStrategy fromMethod(PaymentMethod method) {
        switch (method) {
            case CREDIT_CARD: return new CreditCardStrategy("0000", "00/00", "000");
            case DEBIT_CARD: return new DebitCardStrategy("0000", "0000");
            case INSTITUTIONAL_BILLING: return new InstitutionalBillingStrategy(0L, "N/A");
            default: throw new IllegalArgumentException("Unknown method: " + method);
        }
    }
}
