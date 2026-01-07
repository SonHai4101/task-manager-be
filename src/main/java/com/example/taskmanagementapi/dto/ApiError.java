package com.example.taskmanagementapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiError {
    private int status;
    private String error;
    private String message;
}
