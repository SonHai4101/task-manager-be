package com.example.taskmanagementapi.controller;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanagementapi.dto.audit.AuditLogResponse;
import com.example.taskmanagementapi.entity.User;
import com.example.taskmanagementapi.service.AuditLogService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "5. Audit log"
)
@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {
    private final AuditLogService auditLogService;

    @GetMapping("/tasks/{taskId}")
    public Page<AuditLogResponse> getTaskAuditLogs(
            @PathVariable UUID taskId,
            @ParameterObject
            @Parameter(hidden = true)
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return auditLogService.getTaskAuditLogs(taskId, pageable);
    }

    @GetMapping("/my-logs")
    public Page<AuditLogResponse> getMyAuditLogs(
            @AuthenticationPrincipal User user,
            @ParameterObject
            @Parameter(hidden = true)
            Pageable pageable) {
        return auditLogService.getMyAuditLogs(user, pageable);
    }

}
