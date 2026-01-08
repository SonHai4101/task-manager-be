package com.example.taskmanagementapi.dto.audit;

import java.time.LocalDate;
import java.util.UUID;

public record AuditLogFilterRequest(
        UUID taskId,
        UUID actorId,
        String action,
        LocalDate from,
        LocalDate to) {

}
