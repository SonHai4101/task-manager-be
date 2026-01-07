package com.example.taskmanagementapi.dto.auth;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class RegisterResponse {
    private UUID id;
    private String email;
    private String fullName;
}
