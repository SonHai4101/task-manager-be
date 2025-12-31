package com.example.taskmanagementapi.task;

import com.example.taskmanagementapi.task.dto.CreateTaskRequest;
import com.example.taskmanagementapi.task.dto.TaskFilterRequest;
import com.example.taskmanagementapi.task.dto.TaskResponse;
import com.example.taskmanagementapi.task.dto.UpdateTaskRequest;
import com.example.taskmanagementapi.task.mapper.TaskMapper;
import com.example.taskmanagementapi.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
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
//        System.out.println("AUTH USER = " + user);
        return taskService.getMyTasks(filter, user, pageable);
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

    @PatchMapping("/{id}/restore")
    @Operation(summary = "restore task from trash")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse restoreTask(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        return taskMapper.toResponse(
                taskService.restoreTask(id, user)
        );
    }
}
