package com.example.taskmanagementapi.task.mapper;

import com.example.taskmanagementapi.task.Task;
import com.example.taskmanagementapi.task.dto.TaskResponse;

public class TaskMapper {
    private TaskMapper() {}

    public static TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .createdAt(task.getCreatedAt())
                .build();
    }
}
