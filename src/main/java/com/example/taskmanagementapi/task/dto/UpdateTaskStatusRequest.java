package com.example.taskmanagementapi.task.dto;

import com.example.taskmanagementapi.task.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskStatusRequest {
    @NotNull
    private TaskStatus status;
}
