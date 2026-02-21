package com.shopsphere.repository;

import com.shopsphere.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // Used to check stock before allowing a user to add a product to their cart
    Optional<Inventory> findByProductId(Long productId);
}