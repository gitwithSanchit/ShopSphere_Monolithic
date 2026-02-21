package com.shopsphere.repository;

import com.shopsphere.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Retrieves all items belonging to a specific order for the invoice/details view
    List<OrderItem> findByOrderId(Long orderId);
}