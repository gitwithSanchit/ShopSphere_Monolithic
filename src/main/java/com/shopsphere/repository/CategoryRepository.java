package com.shopsphere.repository;

import com.shopsphere.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Basic CRUD methods are inherited from JpaRepository
}