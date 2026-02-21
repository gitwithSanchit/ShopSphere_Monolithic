package com.shopsphere.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cartid", nullable = false)
    private Long cartId;

    @Column(name = "productid", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;
}