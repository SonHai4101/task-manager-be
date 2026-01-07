package com.example.taskmanagementapi.audit;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.taskmanagementapi.audit.dto.AuditLogResponse;
import com.example.taskmanagementapi.audit.mapper.AuditLogMapper;
import com.example.taskmanagementapi.task.Task;
import com.example.taskmanagementapi.user.User;

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
}
