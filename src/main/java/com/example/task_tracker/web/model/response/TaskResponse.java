package com.example.task_tracker.web.model.response;

import com.example.task_tracker.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private TaskStatus status;

    private UserResponse author;
    private UserResponse assignee;

//    private Set<String> observerIds = new HashSet<>();
    private Set<UserResponse> observers = new HashSet<>();
}
