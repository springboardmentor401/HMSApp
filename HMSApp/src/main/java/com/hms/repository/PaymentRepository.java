package com.hms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hms.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    // Custom queries if needed
}
