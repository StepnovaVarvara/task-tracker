package com.example.task_tracker.service;

import com.example.task_tracker.entity.Task;
import com.example.task_tracker.entity.TaskStatus;
import com.example.task_tracker.repository.TaskRepository;
import com.example.task_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public Flux<Task> findAll() {
        return taskRepository.findAll().flatMap(task -> {
            return findById(task.getId());
        });
    }

    @Override
    public Mono<Task> findById(String taskId) {
       return taskRepository.findById(taskId).flatMap(task -> {
           return userRepository.findById(task.getAuthorId()).doOnNext(task::setAuthor)
                  .then(userRepository.findById(task.getAssigneeId()).doOnNext(task::setAssignee))
                   // TODO добавить наблюдателей
                  .then(taskRepository.save(task));
        });
    }

    @Override
    public Mono<Task> save(Task task) {
        task.setId(UUID.randomUUID().toString());
        task.setCreatedAt(Instant.now());
        task.setStatus(TaskStatus.TODO);

        return userRepository.findById(task.getAuthorId()).doOnNext(task::setAuthor)
                .then(userRepository.findById(task.getAssigneeId()).doOnNext(task::setAssignee))
                // TODO добавить наблюдателей
                .then(taskRepository.save(task));
    }

    @Override
    public Mono<Task> update(String taskId, Task task) {
        return findById(taskId).flatMap(taskForUpdate -> {
            if (StringUtils.hasText(task.getName())) {
                taskForUpdate.setName(task.getName());
            }

            if (StringUtils.hasText(task.getDescription())) {
                taskForUpdate.setDescription(task.getDescription());
            }

            if (task.getCreatedAt() != null) {
                taskForUpdate.setCreatedAt(task.getCreatedAt());
            }

            taskForUpdate.setUpdatedAt(Instant.now());

            if (task.getStatus() != null) {
                taskForUpdate.setStatus(task.getStatus());
            }

            if (StringUtils.hasText(task.getAuthorId())) {
                taskForUpdate.setAuthorId(task.getAuthorId());
            }

            if (StringUtils.hasText(task.getAssigneeId())) {
                taskForUpdate.setAssigneeId(task.getAssigneeId());
            }

            if (task.getObserverIds() != null) {
                taskForUpdate.setObserverIds(task.getObserverIds());
            }

            return userRepository.findById(task.getAuthorId()).doOnNext(taskForUpdate::setAuthor)
                    .then(userRepository.findById(task.getAssigneeId()).doOnNext(taskForUpdate::setAssignee))
                    // TODO добавить наблюдателей
                    .then(taskRepository.save(taskForUpdate));
        });
    }

    @Override
    public Mono<Task> addObserver(String taskId, String userId) {
        return null;
//        Mono<Task> taskMono = findById(taskId);
//
//        return findById(taskId).flatMap(taskForUpdate -> {
//            userRepository.findById(userId).doOnNext(taskForUpdate::setObservers)
//        });
    }

    @Override
    public Mono<Void> deleteById(String taskId) {
        return taskRepository.deleteById(taskId);
    }
}