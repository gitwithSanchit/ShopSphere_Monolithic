package com.shopsphere.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Simple reference, easy to move to Microservices later

    private String street;
    private String city;
    private String state;

    @Column(name = "pin_code")
    private String pincode;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}