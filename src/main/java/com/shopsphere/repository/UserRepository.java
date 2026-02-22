package com.shopsphere.repository;

import com.shopsphere.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Used for Login and fetching Profile
    Optional<User> findByEmail(String email);

    // Used for Login (if you allow login via username)
    Optional<User> findByUsername(String username);

    // Used during Registration to prevent duplicate accounts
    boolean existsByEmail(String email);

    // Used during Registration to ensure unique usernames
    boolean existsByUsername(String username);
}