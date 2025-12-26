package com.example.taskmanagementapi.task;

import com.example.taskmanagementapi.task.dto.TaskResponse;
import com.example.taskmanagementapi.task.dto.UpdateTaskRequest;
import com.example.taskmanagementapi.task.mapper.TaskMapper;
import com.example.taskmanagementapi.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
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

    public Page<TaskResponse> getMyTasks(User user, Pageable pageable) {
        return taskRepository
                .findByOwnerId(user.getId(), pageable)
                .map(TaskMapper::toResponse);
    }

    @Transactional
    public Task updateTask(
            UUID taskId,
            UpdateTaskRequest request,
            User currentUser
    ) {
        Task task = taskRepository.findByIdAndOwnerId(taskId, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

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

        return taskRepository.save(task);
    }

    public void deleteTask(UUID taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        taskRepository.delete(task);
    }
}
