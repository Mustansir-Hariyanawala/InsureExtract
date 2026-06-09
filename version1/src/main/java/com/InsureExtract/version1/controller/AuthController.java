package com.InsureExtract.version1.controller;

import com.InsureExtract.version1.dto.AuthRequest;
import com.InsureExtract.version1.service.JwtService;
import com.InsureExtract.version1.model.UserEntity;
import com.InsureExtract.version1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // Find user by username
        UserEntity user = userService.getUserByUsername(request.getUsername());

        // Verify BCrypt password matches
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // Generate JWT token
        String token = jwtService.generateToken(user.getUsername());

        // Return token in JSON payload
        return ResponseEntity.ok(Map.of("token", token));
    }
}