package com.example.taskmanagementapi.task.dto;

import com.example.taskmanagementapi.task.Priority;
import com.example.taskmanagementapi.task.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskFilterRequest {
    private TaskStatus taskStatus;
    private Priority priority;

    private String keyword;

    private LocalDate from;

    private LocalDate to;
}
