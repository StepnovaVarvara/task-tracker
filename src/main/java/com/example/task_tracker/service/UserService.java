package com.example.task_tracker.service;

import com.example.task_tracker.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<User> findAll();
    Mono<User> findById(String userId);
    Mono<User> save(User user);
    Mono<User> update(String userId, User user);
    Mono<Void> deleteById(String userId);
}
