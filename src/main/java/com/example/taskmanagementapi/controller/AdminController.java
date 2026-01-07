package com.example.taskmanagementapi.controller;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanagementapi.dto.audit.AuditLogResponse;
import com.example.taskmanagementapi.dto.task.TaskResponse;
import com.example.taskmanagementapi.entity.AuditLog;
import com.example.taskmanagementapi.entity.User;
import com.example.taskmanagementapi.repository.AuditLogRepository;
import com.example.taskmanagementapi.service.AuditLogService;
import com.example.taskmanagementapi.service.TaskService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AuditLogRepository auditLogRepository;
    private final TaskService taskService;
    private final AuditLogService auditLogService;

    @GetMapping("/audit-logs")
    public Page<AuditLog> getLogs(
            @ParameterObject @Parameter(hidden = true) @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    @PatchMapping("/{taskId}/assign/{userId}")
    public TaskResponse assignTask(
            @PathVariable UUID taskId,
            @PathVariable UUID userId,
            @AuthenticationPrincipal User currentUser) {
        return taskService.assignTask(taskId, userId, currentUser);
    }

    @PatchMapping("/{taskId}/unassign")
    public TaskResponse unassignTask(
            @PathVariable UUID taskId,
            @AuthenticationPrincipal User currentUser) {
        return taskService.unassignTask(taskId, currentUser);
    }

    @GetMapping("/users/{usersId}")
    public Page<AuditLogResponse> getUserAuditLogAsAdmin(
        @PathVariable UUID userId,
        @ParameterObject
        @Parameter(hidden = true)
        @PageableDefault(
            sort = "createdAt",
            direction = Sort.Direction.DESC
        ) Pageable pageable
    ) {
        return auditLogService.getUserAuditLogAsAdmin(userId, pageable);
    }
}
