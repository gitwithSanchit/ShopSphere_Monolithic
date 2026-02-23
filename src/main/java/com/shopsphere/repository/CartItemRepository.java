package com.shopsphere.repository;

import com.shopsphere.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // To check if a product is already in the cart
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    // To get all items in a specific cart
    List<CartItem> findByCartId(Long cartId);

    // To clear the cart after an order is placed
    void deleteByCartId(Long cartId);
}