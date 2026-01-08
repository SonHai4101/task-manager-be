package com.example.taskmanagementapi.spec;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.taskmanagementapi.dto.audit.AuditLogFilterRequest;
import com.example.taskmanagementapi.entity.AuditLog;

import jakarta.persistence.criteria.Predicate;

public class AuditLogSpecification {
    public static Specification<AuditLog> filter(AuditLogFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by task
            if (filter.taskId() != null) {
                predicates.add(cb.equal(root.get("entityType"), "TASK"));
                predicates.add(cb.equal(root.get("entityId"), filter.taskId()));
            }

            // Filter by user (actor)
            if (filter.actorId() != null) {
                predicates.add(cb.equal(root.get("actorId"), filter.actorId()));
            }

            // Filter by action
            if (filter.from() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("createdAt"), filter.from().atStartOfDay()));
            }

            if (filter.to() != null) {
                predicates.add(
                        cb.equal(root.get("createdAt"), filter.to().atTime(23, 59, 59)));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
