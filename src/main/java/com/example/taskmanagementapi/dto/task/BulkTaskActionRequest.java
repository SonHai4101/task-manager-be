package com.example.taskmanagementapi.dto.task;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record BulkTaskActionRequest(
        @NotEmpty List<UUID> ids
) {}
