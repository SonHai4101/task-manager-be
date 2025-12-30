package com.example.taskmanagementapi.task.spec;

import com.example.taskmanagementapi.task.Task;
import com.example.taskmanagementapi.task.dto.TaskFilterRequest;
import com.example.taskmanagementapi.user.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskSpecification {

    public static Specification<Task> filter(
            TaskFilterRequest filter,
            User user
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // 🔐 Only user’s tasks
            predicates.add(
                    cb.equal(root.get("owner").get("id"), user.getId())
            );

            predicates.add(
                    cb.isNull(root.get("deletedAt"))
            );

            if (filter.getTaskStatus() != null) {
                predicates.add(
                        cb.equal(root.get("status"), filter.getTaskStatus())
                );
            }

            if (filter.getPriority() != null) {
                predicates.add(
                        cb.equal(root.get("priority"), filter.getPriority())
                );
            }

            if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
                String like = "%" + filter.getKeyword().toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("title")), like),
                                cb.like(cb.lower(root.get("description")), like)
                        )
                );
            }

            if (filter.getFrom() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getFrom().atStartOfDay()
                        )
                );
            }

            if (filter.getTo() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getTo().atTime(23, 59, 59)
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Task> trash(User user) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("owner"), user),
                cb.isNotNull(root.get("deletedAt"))
        );
    }
}
