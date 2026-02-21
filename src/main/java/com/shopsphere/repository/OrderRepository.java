package com.shopsphere.repository;

import com.shopsphere.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // To show a user their past orders in their "Account" section
    List<Order> findByUserId(Long userId);

    // For admin use: find all orders that need to be shipped
    List<Order> findByStatus(String status);
}