package com.example.taskmanagementapi.audit.mapper;

import org.mapstruct.Mapper;

import com.example.taskmanagementapi.audit.AuditLog;
import com.example.taskmanagementapi.audit.dto.AuditLogResponse;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {
    AuditLogResponse toResponse(AuditLog log);
}
