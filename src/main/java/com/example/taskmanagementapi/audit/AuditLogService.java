package com.example.taskmanagementapi.audit;

import java.util.UUID;

import org.springframework.stereotype.Service;

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
        User actor
    ) {
        AuditLog log = AuditLog.builder()
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .actorId(entityId)
                .actorEmail(entityType)
                .build();

        auditLogRepository.save(log);
    }

    
}
