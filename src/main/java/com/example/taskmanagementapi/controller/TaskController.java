package com.example.taskmanagementapi.controller;

import com.example.taskmanagementapi.dto.task.BulkTaskActionRequest;
import com.example.taskmanagementapi.dto.task.CreateTaskRequest;
import com.example.taskmanagementapi.dto.task.TaskFilterRequest;
import com.example.taskmanagementapi.dto.task.TaskResponse;
import com.example.taskmanagementapi.dto.task.UpdateTaskRequest;
import com.example.taskmanagementapi.entity.User;
import com.example.taskmanagementapi.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

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

    @Operation(summary = "Get my tasks")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskResponse> getMyTask(
            @ParameterObject
            @Valid TaskFilterRequest filter,
            @Parameter(hidden = true)
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            @AuthenticationPrincipal User user
    ) {

        return taskService.getMyTasks(filter, user, pageable);
    }

    @Operation(summary = "Get tasks assigned to me")
    @GetMapping("/assigned")
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskResponse> getAssignedTask(
        @ParameterObject
        @Valid TaskFilterRequest filter,
        @Parameter(hidden = true)
        @PageableDefault(
                size = 10,
                sort = "createdAt",
                direction = Sort.Direction.DESC
        ) Pageable pageable,
        @AuthenticationPrincipal User user
    ) {
        return taskService.getAssignedTasks(filter, user, pageable);
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

    @Operation(summary = "Get my trash tasks")
    @GetMapping("/trash")
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskResponse> getTrashTasks(
            @ParameterObject
            @Parameter(hidden = true)
            @PageableDefault(
                    size = 10,
                    sort = "deletedAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable,
            @AuthenticationPrincipal User user
    ) {
        return taskService.getTrashTasks(user, pageable);
    }

    @PatchMapping("/restore")
    @Operation(summary = "Restore multiple tasks from trash")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void restoreTask(
            @Valid @RequestBody BulkTaskActionRequest request,
            @AuthenticationPrincipal User user
    ) {
        taskService.restoreTasks(request.ids(), user);
    }

    @DeleteMapping("/trash")
    @Operation(
            summary = "Permanently delete tasks from trash",
            description = "This action is irreversible"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void permanentlyDeleteTasks(
            @Valid @RequestBody BulkTaskActionRequest request,
            @AuthenticationPrincipal User user
    ) {
        taskService.permanentlyDeleteTasks(request.ids(), user);
    }

    
}
