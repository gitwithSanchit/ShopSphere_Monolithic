package com.shopsphere.service;

import com.shopsphere.model.User;
import com.shopsphere.model.Cart;
import com.shopsphere.model.UserRole;
import com.shopsphere.repository.UserRepository;
import com.shopsphere.repository.RoleRepository;
import com.shopsphere.repository.UserRoleRepository;
import com.shopsphere.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Inject the bean here

    // We use @Transactional to ensure that if Cart creation fails,
    // the User isn't saved either. (All or nothing)
    @Transactional
    public User registerNewUser(User user) {
        // 1. Check if email/username already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already in use!");
        }

        // 2. Hash the password (using a placeholder for now, implement BCrypt later)
        // We take the plain text from the DTO and replace it with a 60-character hash
        String encodedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(encodedPassword);

        // 3. Save the User
        User savedUser = userRepository.save(user);

        // 4. Assign Default Role (ROLE_CUSTOMER)
        roleRepository.findByName("ROLE_CUSTOMER").ifPresent(role -> {
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

    public User loginUser(String email, String rawPassword) {
        // 1. Fetch user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // 2. Check if the raw password matches the encoded hash in DB
        if (passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            return user;
        } else {
            throw new RuntimeException("Invalid email or password");
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}