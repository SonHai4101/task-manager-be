package com.example.taskmanagementapi.task.mapper;

import com.example.taskmanagementapi.task.Task;
import com.example.taskmanagementapi.task.dto.TaskResponse;
import com.example.taskmanagementapi.task.dto.UpdateTaskRequest;
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
