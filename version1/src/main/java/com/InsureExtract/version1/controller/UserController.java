package com.InsureExtract.version1.controller;

import com.InsureExtract.version1.model.UserEntity;
import com.InsureExtract.version1.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * POST /api/v1/users/register
     * Registers a new user.
     */
    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerUser(@Valid @RequestBody UserEntity user) {
        UserEntity createdUser = userService.registerUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * GET /api/v1/users/username/{username}
     * Fetches a single user by their username.
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserEntity> getUserByUsername(@PathVariable String username) {
        UserEntity user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * GET /api/v1/users/{id}
     * Fetches a single user by their UUID primary key.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable("id") String userId) {
        UserEntity user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * GET /api/v1/users
     * Fetches a list of all registered users.
     */
    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * DELETE /api/v1/users/{id}
     * Deletes a user from the system by their UUID primary key.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok("User deleted successfully with ID: " + userId);
    }
}