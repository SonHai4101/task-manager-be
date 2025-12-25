package com.example.taskmanagementapi.task;

import com.example.taskmanagementapi.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task createTask(String title, String description, User user) {
        Task task = Task.builder()
                .title(title)
                .description(description)
                .status(TaskStatus.TODO)
                .owner(user)
                .build();

        return taskRepository.save(task);
    }

    public List<Task> getMyTasks(User user) {
        return taskRepository.findByOwnerId(user.getId());
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
