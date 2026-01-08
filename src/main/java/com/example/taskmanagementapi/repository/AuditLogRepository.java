package com.example.taskmanagementapi.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.taskmanagementapi.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID>,
        JpaSpecificationExecutor<AuditLog> {
    Page<AuditLog> findByEntityTypeAndEntityId(
            String entityType,
            UUID entityId,
            Pageable pageable);

    Page<AuditLog> findByActorId(
            UUID actorId,
            Pageable pageable);
}
