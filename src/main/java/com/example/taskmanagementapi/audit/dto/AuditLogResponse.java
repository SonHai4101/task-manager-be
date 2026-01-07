package com.example.taskmanagementapi.audit.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogResponse {
    private UUID id;
    private String action;
    private String entityType;
    private UUID entityId;
    
    private UUID actorId;
    private String actorEmail;

    private UUID targetUserId;
    private String targetUserEmail;

    private String message;
    private LocalDateTime createdAt;
}
