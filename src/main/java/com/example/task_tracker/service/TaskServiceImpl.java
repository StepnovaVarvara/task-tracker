package com.example.task_tracker.service;

import com.example.task_tracker.entity.Task;
import com.example.task_tracker.entity.TaskStatus;
import com.example.task_tracker.entity.User;
import com.example.task_tracker.repository.TaskRepository;
import com.example.task_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public Flux<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Mono<Task> findById(String taskId) {
        return taskRepository.findById(taskId);
    }

    @Override
    public Mono<Task> save(Task task) {
        task.setId(UUID.randomUUID().toString());
        task.setCreatedAt(Instant.now());
        task.setStatus(TaskStatus.TODO);

        return taskRepository.save(task)
                .then(usersInTaskModel(task));
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

            return taskRepository.save(taskForUpdate)
                    .then(usersInTaskModel(taskForUpdate));
        });
    }

    @Override
    public Mono<Task> addObserver(String taskId, String userId) {
        Mono<Task> taskMono = taskRepository.findById(taskId);
        Mono<User> userMono = userRepository.findById(userId);

        taskMono.map(task -> {
            task.getObservers();
            return task;
        });

        return null;
    }

    @Override
    public Mono<Void> deleteById(String taskId) {
        return taskRepository.deleteById(taskId);
    }

    private Mono<Task> usersInTaskModel(Task task) {
        Mono<Task> authorMono = setAuthor(task);
        Mono<Task> assigneeMono = setAssignee(task);
        Mono<Task> observersMono = setObservers(task);

        return Mono.zip(authorMono, assigneeMono, observersMono)
                .map(user -> {
                    Task modelWithAuthor = user.getT1();
                    Task modelWithAssignee = user.getT2();
                    Task modelWithObservers = user.getT3();

                    modelWithAuthor.setAssignee(modelWithAssignee.getAssignee());
                    modelWithAuthor.setObservers(modelWithObservers.getObservers());

                    return modelWithAuthor;
                });
    }

    private Mono<Task> setAuthor(Task task) {
        String authorId = task.getAuthorId();

        if (authorId == null || authorId.isEmpty()) {
            return Mono.just(task);
        }

        return userRepository.findById(authorId)
                .map(user -> {
                    task.setAuthor(user);
                    return task;
                });
    }

    private Mono<Task> setAssignee(Task task) {
        String assigneeId = task.getAssigneeId();

        if (assigneeId == null || assigneeId.isEmpty()) {
            return Mono.just(task);
        }

        return userRepository.findById(assigneeId)
                .map(user -> {
                    task.setAssignee(user);
                    return task;
                });
    }

    public Mono<Task> setObservers(Task task) {
        Set<String> observerIds = task.getObserverIds();

        if (observerIds == null || observerIds.isEmpty()) {
            return Mono.just(task);
        }

        Set<Mono<User>> observerMonos = observerIds.stream()
                .map(userRepository::findById)
                .collect(Collectors.toSet());

        return Flux.fromIterable(observerMonos)
                .flatMap(mono -> mono)
                .collectList()
                .map(users -> {
                    task.setObservers(new HashSet<>(users));
                    return task;
                });
    }
}