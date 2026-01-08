package com.example.taskmanagementapi.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.taskmanagementapi.dto.audit.AuditLogFilterRequest;
import com.example.taskmanagementapi.dto.audit.AuditLogResponse;
import com.example.taskmanagementapi.entity.AuditLog;
import com.example.taskmanagementapi.entity.Task;
import com.example.taskmanagementapi.entity.User;
import com.example.taskmanagementapi.mapper.AuditLogMapper;
import com.example.taskmanagementapi.repository.AuditLogRepository;
import com.example.taskmanagementapi.spec.AuditLogSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public void log(
            String action,
            String entityType,
            UUID entityId,
            User actor) {
        AuditLog log = AuditLog.builder()
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .actorId(actor.getId())
                .actorEmail(actor.getEmail())
                .build();

        auditLogRepository.save(log);
    }

    public void logTaskAssigned(
            Task task,
            User actor,
            User assignedTo) {
        AuditLog log = AuditLog.builder()
                .action("TASK_ASSIGNED")
                .entityType("TASK")
                .entityId(task.getId())
                .actorId(actor.getId())
                .actorEmail(actor.getEmail())
                .targetUserId(assignedTo.getId())
                .targetUserEmail(assignedTo.getEmail())
                .message("Task '" + task.getTitle() + "' assigned to " + assignedTo.getEmail())
                .build();
        auditLogRepository.save(log);
    }

    public Page<AuditLogResponse> getTaskAuditLogs(
            UUID taskId,
            Pageable pageable) {
        return auditLogRepository
                .findByEntityTypeAndEntityId("TASK", taskId, pageable)
                .map(auditLogMapper::toResponse);
    }

    public Page<AuditLogResponse> getMyAuditLogs(
            User user,
            Pageable pageable) {
        return auditLogRepository
                .findByActorId(user.getId(), pageable)
                .map(auditLogMapper::toResponse);
    }

    // Admin only
    public Page<AuditLogResponse> getUserAuditLogAsAdmin(
        UUID userId,
        Pageable pageable
    ) {
        return auditLogRepository
                .findByActorId(userId, pageable)
                .map(auditLogMapper::toResponse);
    }

    public Page<AuditLog> getLogs(
        AuditLogFilterRequest filter,
        Pageable pageable
    ) {
        return auditLogRepository.findAll(
            AuditLogSpecification.filter(filter),
            pageable
        );
    }
}
