package com.example.taskmanagementapi.task;

import com.example.taskmanagementapi.audit.AuditLogService;
import com.example.taskmanagementapi.common.exception.PageableUtil;
import com.example.taskmanagementapi.task.dto.TaskFilterRequest;
import com.example.taskmanagementapi.task.dto.TaskResponse;
import com.example.taskmanagementapi.task.dto.UpdateTaskRequest;
import com.example.taskmanagementapi.task.mapper.TaskMapper;
import com.example.taskmanagementapi.task.spec.TaskSpecification;
import com.example.taskmanagementapi.user.User;
import com.example.taskmanagementapi.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final AuditLogService auditLogService;
    private final UserRepository userRepository;

    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .createdAt(task.getCreatedAt())
                .build();
    }

    public TaskResponse createTask(String title, String description, Priority priority, User user) {
        Task task = Task.builder()
                .title(title)
                .description(description)
                .status(TaskStatus.TODO)
                .priority(priority != null ? priority : Priority.MEDIUM)
                .owner(user)
                .build();

        return toResponse(taskRepository.save(task));
    }

    public Page<TaskResponse> getMyTasks(
            TaskFilterRequest filter,
            User user,
            Pageable pageable) {
        Pageable finalPageable = PageableUtil.withDefaultSort(pageable, "createdAt");
        return taskRepository
                .findAll(TaskSpecification.filter(filter, user), finalPageable)
                .map(taskMapper::toResponse);
    }

    @Transactional
    public TaskResponse updateTask(
            UUID taskId,
            UpdateTaskRequest request,
            User currentUser) {
        Task task = taskRepository.findByIdAndOwnerId(taskId, currentUser.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Task not found"));

        if (task.getDeletedAt() != null) {
            throw new IllegalArgumentException("Cannot update a deleted task");
        }

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }

        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }

        taskMapper.updateTaskFromRequest(request, task);
        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(UUID taskId, User user) {
        Task task = taskRepository.findByIdAndOwnerId(taskId, user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Task not found or you are not the owner"));

        task.setDeletedAt(LocalDateTime.now());

        auditLogService.log("DELETE_TASK", "TASK", task.getId(), user);
    }

    public Page<TaskResponse> getTrashTasks(
            User user,
            Pageable pageable) {
        Pageable finalPageable = PageableUtil.withDefaultSort(pageable, "deletedAt");

        return taskRepository
                .findAll(TaskSpecification.trash(user), finalPageable)
                .map(taskMapper::toResponse);
    }

    @Transactional
    public void restoreTasks(List<UUID> ids, User user) {
        List<Task> tasks = taskRepository
                .findAllByIdInAndOwnerIdAndDeletedAtIsNotNull(ids, user.getId());

        if (tasks.isEmpty()) {
            throw new IllegalArgumentException("No tasks found in trash");
        }

        // tasks.forEach(task -> task.setDeletedAt(null));
        for (Task task : tasks) {
            task.setDeletedAt(null);

            auditLogService.log("RESTORE_TASK", "TASK", task.getId(), user);
        }
    }

    @Transactional
    public void permanentlyDeleteTasks(List<UUID> ids, User user) {
        taskRepository.deleteAllByIdInAndOwnerIdAndDeletedAtIsNotNull(
                ids,
                user.getId());
    }

    @Transactional
    public TaskResponse assignTask(
            UUID taskId,
            UUID assigneeId,
            User currentUser) {
        Task task = taskRepository.findByIdAndOwnerId(taskId, currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found or not owned by user"));

        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new IllegalArgumentException("Assignee not found"));

        task.setAssignedTo(assignee);

        return taskMapper.toResponse(task);
    }

    @Transactional
    public TaskResponse unassignTask(UUID taskId, User currentUser) {
        Task task = taskRepository.findByIdAndOwnerId(taskId, currentUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("Task not found or not owned by user"));

        task.setAssignedTo(null);

        return taskMapper.toResponse(task);
    }

    public Page<TaskResponse> getAssignedTasks(
        TaskFilterRequest filter,
        User user,
        Pageable pageable
    ) {
        Pageable finalPageable = 
            PageableUtil.withDefaultSort(pageable, "createdAt");

        return taskRepository
                .findAll(
                    TaskSpecification.assignedToMe(filter, user),
                    finalPageable
                ).map(taskMapper::toResponse);
    }
}
