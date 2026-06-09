package com.InsureExtract.version1.service;

import com.InsureExtract.version1.config.SecurityConfig;
import com.InsureExtract.version1.model.UserEntity;
import com.InsureExtract.version1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user into the system after validating uniqueness.
     */
    @Transactional
    public UserEntity registerUser(UserEntity newUser) {
        // 1. Validate uniqueness
        if (userRepository.existsByUsername(newUser.getUsername())) {
            throw new RuntimeException("Error: Username '" + newUser.getUsername() + "' is already taken!");
        }

        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new RuntimeException("Error: Email '" + newUser.getEmail() + "' is already registered!");
        }

        // 2. Hash the plain-text password using BCrypt
        String encryptedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encryptedPassword);

        // 3. Save the entity with the secured password
        return userRepository.save(newUser);
    }

    /**
     * Retrieves a user by their unique username.
     */
    @Transactional(readOnly = true)
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    /**
     * Retrieves a user by their auto-generated UUID primary key.
     */
    @Transactional(readOnly = true)
    public UserEntity getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    /**
     * Retrieves all registered users.
     */
    @Transactional(readOnly = true)
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user by their unique UUID primary key.
     */
    @Transactional
    public void deleteUserById(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Cannot delete. User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }
}