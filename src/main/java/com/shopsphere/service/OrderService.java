package com.shopsphere.service;

import com.shopsphere.model.*;
import com.shopsphere.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public Order placeOrder(Long userId, Long cartId, Long addressId) {
        // 1. Fetch all items from the User's Cart
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot place order: Cart is empty.");
        }

        // 2. Initialize the Order object
        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setStatus("PENDING");

        // Calculate total amount using BigDecimal math
        BigDecimal total = cartItems.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        // Save the parent Order first to get the generated ID
        Order savedOrder = orderRepository.save(order);

        // 3. Process each CartItem into an OrderItem
        for (CartItem cartItem : cartItems) {
            // Check & Deduct Inventory - This is the "Safety Gate"
            boolean stockDeducted = inventoryService.deductStock(cartItem.getProductId(), cartItem.getQuantity());

            if (!stockDeducted) {
                throw new RuntimeException("Insufficient stock for Product ID: " + cartItem.getProductId());
            }

            // Create OrderItem record (History)
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(savedOrder.getId());
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice()); // Locking the purchase price

            orderItemRepository.save(orderItem);
        }

        // 4. Success! Clear the Cart
        cartItemRepository.deleteByCartId(cartId);

        return savedOrder;
    }
}