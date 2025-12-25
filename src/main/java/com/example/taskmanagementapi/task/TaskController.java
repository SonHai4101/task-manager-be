package com.example.taskmanagementapi.task;

import com.example.taskmanagementapi.task.dto.CreateTaskRequest;
import com.example.taskmanagementapi.task.dto.UpdateTaskStatusRequest;
import com.example.taskmanagementapi.user.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(
            @Valid @RequestBody CreateTaskRequest request,
            @AuthenticationPrincipal User user
    ) {
        return taskService.createTask(
                request.getTitle(),
                request.getDesctiption(),
                user
        );
    }

    @GetMapping
    public List<Task> getMyTask(@AuthenticationPrincipal User user) {
        System.out.println("AUTH USER = " + user);
        return taskService.getMyTasks(user);
    }

    @PatchMapping("/{id}/status")
    public Task updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskStatusRequest request,
            @AuthenticationPrincipal User user
    ) {
        return taskService.updateStatus(id, request.getStatus(), user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        taskService.deleteTask(id, user);
    }
}
