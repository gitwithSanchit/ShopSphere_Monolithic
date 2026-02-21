package com.shopsphere.repository;

import com.shopsphere.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    // Find all role assignments for a specific user
    List<UserRole> findByUserId(Long userId);

    // To check if a user already has a specific role (prevents duplicates)
    boolean existsByUserIdAndRoleId(Long userId, Long roleId);
}