package com.example.taskmanagementapi.service;

import com.example.taskmanagementapi.dto.user.UserResponse;
import com.example.taskmanagementapi.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    public UserResponse getCurrentUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullname(user.getFullname())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
