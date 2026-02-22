package com.shopsphere.service;

import com.shopsphere.model.User;
import com.shopsphere.model.Cart;
import com.shopsphere.model.UserRole;
import com.shopsphere.repository.UserRepository;
import com.shopsphere.repository.RoleRepository;
import com.shopsphere.repository.UserRoleRepository;
import com.shopsphere.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private CartRepository cartRepository;

    // We use @Transactional to ensure that if Cart creation fails,
    // the User isn't saved either. (All or nothing)
    @Transactional
    public User registerNewUser(User user) {
        // 1. Check if email/username already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already in use!");
        }

        // 2. Hash the password (using a placeholder for now, implement BCrypt later)
        // user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        // 3. Save the User
        User savedUser = userRepository.save(user);

        // 4. Assign Default Role (ROLE_USER)
        roleRepository.findByName("ROLE_USER").ifPresent(role -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(savedUser.getId());
            userRole.setRoleId(role.getId());
            userRoleRepository.save(userRole);
        });

        // 5. Initialize an Empty Cart for the user
        Cart cart = new Cart();
        cart.setUserId(savedUser.getId());
        cartRepository.save(cart);

        return savedUser;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}