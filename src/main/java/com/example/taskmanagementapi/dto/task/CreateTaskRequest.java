package com.example.taskmanagementapi.dto.task;

import com.example.taskmanagementapi.entity.Priority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskRequest {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Priority priority;
}
