package com.example.taskmanagementapi.task.dto;

import com.example.taskmanagementapi.task.Priority;
import com.example.taskmanagementapi.task.TaskStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateTaskRequest {

    @Size
    private String title;

    private String description;

    private TaskStatus status;

    private Priority priority;

    private LocalDateTime dueDate;
}
