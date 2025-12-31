package com.example.taskmanagementapi.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository
        extends JpaRepository<Task, UUID>,
                JpaSpecificationExecutor<Task> {
    Page<Task> findByOwnerId(UUID owner_id, Pageable pageable);

    Optional<Task> findByIdAndOwnerId(UUID id, UUID owner_id);

    List<Task> findAllByIdInAndOwnerIdAndDeletedAtIsNotNull(
            List<UUID> ids,
            UUID ownerId
    );

    void deleteAllByIdInAndOwnerIdAndDeletedAtIsNotNull(
            List<UUID> ids,
            UUID ownerId
    );

    void deleteAllByDeletedAtBefore(LocalDateTime threshold);

    boolean existsByIdAndOwnerId(UUID id, UUID owner_id);
}
