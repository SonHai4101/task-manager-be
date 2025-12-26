package com.example.taskmanagementapi.task;

import com.example.taskmanagementapi.task.dto.TaskResponse;
import com.example.taskmanagementapi.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public List<TaskResponse> getMyTasks(User user) {
        return taskRepository.findByOwnerId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Task updateStatus(UUID taskId, TaskStatus status, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        task.setStatus(status);
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
