package com.shopsphere.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String street;
    private String city;
    private String state;

    @Column(name = "pin_code")
    private String pincode;

    @Column(name = "is_default")
    private Boolean isDefault;
}