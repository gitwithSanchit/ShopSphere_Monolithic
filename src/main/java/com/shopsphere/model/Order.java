package com.shopsphere.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userid", nullable = false)
    private Long userId;

    @Column(name = "orderdate", updatable = false)
    private LocalDateTime orderDate;

    @Column(name = "totalamount", nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private String status; // e.g., 'PENDING', 'COMPLETED', 'CANCELLED'

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
        if (this.status == null) {
            this.status = "PENDING"; // Default status for new orders
        }
    }
}