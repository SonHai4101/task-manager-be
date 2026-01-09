package com.example.taskmanagementapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanagementapi.dto.auth.LoginRequest;
import com.example.taskmanagementapi.dto.auth.LoginResponse;
import com.example.taskmanagementapi.dto.auth.RegisterRequest;
import com.example.taskmanagementapi.dto.auth.RegisterResponse;
import com.example.taskmanagementapi.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "2. Auth"
)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }
}
