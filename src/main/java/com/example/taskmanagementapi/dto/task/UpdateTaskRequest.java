package com.example.taskmanagementapi.dto.task;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.example.taskmanagementapi.entity.Priority;
import com.example.taskmanagementapi.entity.TaskStatus;

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
