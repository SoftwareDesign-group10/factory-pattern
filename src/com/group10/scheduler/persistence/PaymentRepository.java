package com.group10.scheduler.persistence;

import com.group10.scheduler.domain.Payment;
import java.util.List;

public interface PaymentRepository {
    List<Payment> loadPayments();
    void savePayments(List<Payment> payments);
}
