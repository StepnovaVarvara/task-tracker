package com.example.task_tracker.mapper;

import com.example.task_tracker.entity.Task;
import com.example.task_tracker.entity.User;
import com.example.task_tracker.web.model.request.TaskRequest;
import com.example.task_tracker.web.model.response.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class})
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    Task requestToTask(TaskRequest request);

    @Mapping(source = "taskId", target = "id")
    Task requestToTask(String taskId, TaskRequest request);

    @Mapping(source = "task", target = "author", qualifiedByName = "authorToAuthor")
    @Mapping(source = "task", target = "assignee", qualifiedByName = "assigneeToAssignee")
    TaskResponse taskToResponse(Task task);
    @Named("authorToAuthor")
    default User authorToAuthor(Task task) {
        return task.getAuthor();
    }
    @Named("assigneeToAssignee")
    default User assigneeToAssignee(Task task) {
        return task.getAssignee();
    }
//    @Named("observerToObserverIdsSet")
//    default Set<String>

}
