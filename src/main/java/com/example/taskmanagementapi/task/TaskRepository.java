package com.example.taskmanagementapi.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findByOwnerId(UUID owner_id, Pageable pageable);

    Optional<Task> findByIdAndOwnerId(UUID id, UUID owner_id);
}
