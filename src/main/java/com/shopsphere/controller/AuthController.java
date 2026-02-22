package com.shopsphere.controller;

import com.shopsphere.dto.LoginRequest;
import com.shopsphere.dto.UserDTO;
import com.shopsphere.model.User;
import com.shopsphere.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDto) {
        try {
            // Map DTO to Entity
            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            user.setPasswordHash(userDto.getPassword()); // In next step, we add BCrypt here
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());

            User registeredUser = userService.registerNewUser(user);
            return ResponseEntity.ok("User registered successfully with ID: " + registeredUser.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

            // For now, we return a success message.
            // Later, this is where we will generate a JWT Token.
            return ResponseEntity.ok("Login successful! Welcome " + user.getFirstName());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}