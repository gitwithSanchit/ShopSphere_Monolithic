package com.shopsphere.repository;

import com.shopsphere.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Used to retrieve the current active cart for a logged-in user
    Optional<Cart> findByUserId(Long userId);
}