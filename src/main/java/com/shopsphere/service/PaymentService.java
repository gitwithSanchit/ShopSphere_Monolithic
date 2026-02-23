package com.shopsphere.service;

import com.shopsphere.model.Order;
import com.shopsphere.model.Payment;
import com.shopsphere.repository.OrderRepository;
import com.shopsphere.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Payment processPayment(Long orderId, String paymentMethod) {
        // 1. Verify the Order exists
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // 2. Create the Payment Record
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(order.getTotalAmount()); // Using the total calculated in Order Module
        payment.setMethod(paymentMethod); // Matches your model field 'method'

        // Simulating a real gateway response
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setStatus("SUCCESS");

        // 3. Update Order Status (The State Change)
        order.setStatus("COMPLETED");
        orderRepository.save(order);

        // 4. Save and return the Payment details
        return paymentRepository.save(payment);
    }
}