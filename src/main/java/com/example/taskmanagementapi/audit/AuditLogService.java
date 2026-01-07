package com.example.taskmanagementapi.audit;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.taskmanagementapi.task.Task;
import com.example.taskmanagementapi.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

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

}
