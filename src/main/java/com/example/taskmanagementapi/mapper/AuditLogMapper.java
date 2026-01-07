package com.example.taskmanagementapi.mapper;

import org.mapstruct.Mapper;

import com.example.taskmanagementapi.dto.audit.AuditLogResponse;
import com.example.taskmanagementapi.entity.AuditLog;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {
    AuditLogResponse toResponse(AuditLog log);
}
