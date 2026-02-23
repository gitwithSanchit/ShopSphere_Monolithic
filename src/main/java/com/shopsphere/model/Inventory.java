package com.shopsphere.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable;

    @Column(name = "low_stock_threshold")
    private Integer lowStockThreshold = 5; // Default to 5 if not specified

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    // Automatically update the timestamp whenever the record is saved or modified
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    public boolean isLowStock() {
        return this.quantityAvailable <= this.lowStockThreshold;
    }
}