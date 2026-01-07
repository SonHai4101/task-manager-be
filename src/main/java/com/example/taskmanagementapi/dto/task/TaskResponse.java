package com.example.taskmanagementapi.dto.task;

import com.example.taskmanagementapi.entity.Priority;
import com.example.taskmanagementapi.entity.TaskStatus;
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
        "assignedToId",
        "assignedToEmail",
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

    private UUID assignedToId;
    private String assignedToEmail;

    private LocalDateTime createdAt;
}
