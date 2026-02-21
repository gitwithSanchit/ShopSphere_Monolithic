package com.shopsphere.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// Update your Category.java
@Entity
@Table(name = "categories")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Column(name = "parent_id")
    private Long parentId;

    // This field won't be in the DB table, but will be filled by Java for the JSON response
    @Transient
    private List<Category> children = new ArrayList<>();
}