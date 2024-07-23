package com.example.task_tracker.web.controller;

import com.example.task_tracker.mapper.TaskMapper;
import com.example.task_tracker.service.TaskService;
import com.example.task_tracker.web.model.request.TaskRequest;
import com.example.task_tracker.web.model.response.TaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Flux<TaskResponse> getAllTasks() {
        return taskService.findAll()
                .map(TaskMapper.INSTANCE::taskToResponse);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Mono<TaskResponse> getTaskById(@PathVariable String id) {
        return taskService.findById(id)
                .map(TaskMapper.INSTANCE::taskToResponse);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<TaskResponse> createTask(@RequestBody @Valid TaskRequest request) {
        return taskService.save(TaskMapper.INSTANCE.requestToTask(request))
                .map(TaskMapper.INSTANCE::taskToResponse);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Mono<TaskResponse> updateTask(@PathVariable String id, @RequestBody @Valid TaskRequest request) {
        return taskService.update(id, TaskMapper.INSTANCE.requestToTask(id, request))
                .map(TaskMapper.INSTANCE::taskToResponse);
    }


    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/observer/{id}")
    public Mono<TaskResponse> addObserverToTask(@PathVariable String id, @RequestParam String userId) {
        return taskService.addObserver(id, userId)
                .map(TaskMapper.INSTANCE::taskToResponse);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public Mono<Void> deleteTaskById(@PathVariable String id) {
        return taskService.deleteById(id);
    }
}
