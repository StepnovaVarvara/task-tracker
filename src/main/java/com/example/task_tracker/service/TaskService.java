package com.example.task_tracker.service;

import com.example.task_tracker.entity.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {
    Flux<Task> findAll();
    Mono<Task> findById(String taskId);
    Mono<Task> save(Task task);
    Mono<Task> update(String taskId, Task task);
    Mono<Task> addObserver(String taskId, String userId);
    Mono<Void> deleteById(String taskId);
}
