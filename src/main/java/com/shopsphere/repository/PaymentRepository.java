package com.shopsphere.repository;

import com.shopsphere.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Used to check if a specific order has a successful payment record
    Optional<Payment> findByOrderId(Long orderId);
}