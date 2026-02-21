package com.shopsphere.repository;

import com.shopsphere.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Crucial for displaying the list of items inside a user's cart
    List<CartItem> findByCartId(Long cartId);

    // Useful for checking if a product is already in the cart so we can just increment quantity
    CartItem findByCartIdAndProductId(Long cartId, Long productId);
}