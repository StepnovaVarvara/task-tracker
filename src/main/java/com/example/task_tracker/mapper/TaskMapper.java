package com.example.task_tracker.mapper;

import com.example.task_tracker.entity.Task;
import com.example.task_tracker.web.model.request.TaskRequest;
import com.example.task_tracker.web.model.response.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class})
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    Task requestToTask(TaskRequest request);

    @Mapping(source = "taskId", target = "id")
    Task requestToTask(String taskId, TaskRequest request);

    TaskResponse taskToResponse(Task task);
}
