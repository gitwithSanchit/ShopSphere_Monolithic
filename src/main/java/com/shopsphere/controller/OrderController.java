package com.shopsphere.controller;

import com.shopsphere.model.Order;
import com.shopsphere.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(
            @RequestParam Long userId,
            @RequestParam Long cartId,
            @RequestParam Long addressId) {
        try {
            Order order = orderService.placeOrder(userId, cartId, addressId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            // Returns a 400 Bad Request if stock is insufficient or cart is empty
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Returns a 500 Internal Server Error for unexpected issues
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }
}