package com.tripnest.controller;

import com.tripnest.dto.AuthDtos.AuthResponse;
import com.tripnest.dto.AuthDtos.Login;
import com.tripnest.dto.AuthDtos.Register;
import com.tripnest.entity.Role;
import com.tripnest.entity.User;
import com.tripnest.repository.UserRepository;
import com.tripnest.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody Register req) {
        String email = req.email().trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(email)) throw new IllegalArgumentException("Email already registered");
        User u = users.save(new User(req.name().trim(), email, encoder.encode(req.password()), Role.CUSTOMER));
        return ResponseEntity.status(201).body(token(u));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody Login req) {
        User u = users.findByEmailIgnoreCase(req.email().trim()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!encoder.matches(req.password(), u.password)) throw new IllegalArgumentException("Invalid credentials");
        return token(u);
    }

    private AuthResponse token(User u) {
        return new AuthResponse(jwt.generate(u.email), u.id, u.name, u.email, u.role.name());
    }
}
