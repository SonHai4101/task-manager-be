package com.example.taskmanagementapi.task;

import com.example.taskmanagementapi.task.dto.CreateTaskRequest;
import com.example.taskmanagementapi.task.dto.TaskResponse;
import com.example.taskmanagementapi.task.dto.UpdateTaskRequest;
import com.example.taskmanagementapi.task.mapper.TaskMapper;
import com.example.taskmanagementapi.user.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final TaskMapper taskMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(
            @Valid @RequestBody CreateTaskRequest request,
            @AuthenticationPrincipal User user
    ) {
        return taskService.createTask(
                request.getTitle(),
                request.getDescription(),
                request.getPriority(),
                user
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskResponse> getMyTask(@AuthenticationPrincipal User user, Pageable pageable) {
        System.out.println("AUTH USER = " + user);
        return taskService.getMyTasks(user, pageable);
    }

    @PatchMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskRequest request,
            @AuthenticationPrincipal User user
    ) {
        return taskService.updateTask(id, request, user);
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
