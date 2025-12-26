package com.example.taskmanagementapi.task.dto;

import com.example.taskmanagementapi.task.Priority;
import com.example.taskmanagementapi.task.TaskStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "status",
        "priority",
        "createdAt"
})

@Getter
@Setter
@Builder
public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private LocalDateTime createdAt;
}
