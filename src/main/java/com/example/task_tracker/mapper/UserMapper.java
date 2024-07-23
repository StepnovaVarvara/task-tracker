package com.example.task_tracker.mapper;

import com.example.task_tracker.entity.User;
import com.example.task_tracker.web.model.request.UserRequest;
import com.example.task_tracker.web.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User requestToUser(UserRequest request);

    @Mapping(source = "userId", target = "id")
    User requestToUser(String userId, UserRequest request);

   UserResponse userToResponse(User user);
}
