package com.example.taskmanagementapi.mapper;

import com.example.taskmanagementapi.dto.task.TaskResponse;
import com.example.taskmanagementapi.dto.task.UpdateTaskRequest;
import com.example.taskmanagementapi.entity.Task;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "assignedToId", source = "assignedTo.id")
    @Mapping(target = "assignedToEmail", source = "assignedTo.email")
    TaskResponse toResponse(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromRequest(UpdateTaskRequest request,
            @MappingTarget Task task);
}
