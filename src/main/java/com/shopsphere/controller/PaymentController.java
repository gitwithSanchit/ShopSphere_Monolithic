package com.shopsphere.controller;

import com.shopsphere.model.Payment;
import com.shopsphere.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<?> processPayment(
            @RequestParam Long orderId,
            @RequestParam String paymentMethod) {
        try {
            // Trigger the service logic to create payment and update order status
            Payment payment = paymentService.processPayment(orderId, paymentMethod);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            // Returns 400 Bad Request for business logic failures (e.g., Order Not Found)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Returns 500 for unexpected system errors
            return ResponseEntity.internalServerError().body("An error occurred during payment: " + e.getMessage());
        }
    }
}