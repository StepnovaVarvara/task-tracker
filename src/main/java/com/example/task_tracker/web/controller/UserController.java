package com.example.task_tracker.web.controller;

import com.example.task_tracker.entity.User;
import com.example.task_tracker.mapper.UserMapper;
import com.example.task_tracker.service.UserService;
import com.example.task_tracker.web.model.request.UserRequest;
import com.example.task_tracker.web.model.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Flux<UserResponse> getAllUsers() {
        return userService.findAll()
                .map(UserMapper.INSTANCE::userToResponse);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Mono<UserResponse> getUserById(@PathVariable String id) {
        return userService.findById(id)
                .map(UserMapper.INSTANCE::userToResponse);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        return userService.save(UserMapper.INSTANCE.requestToUser(request))
                .map(UserMapper.INSTANCE::userToResponse);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Mono<UserResponse> updateUser(@PathVariable String id, @RequestBody @Valid UserRequest request) {
        return userService.update(id, UserMapper.INSTANCE.requestToUser(id, request))
                .map(UserMapper.INSTANCE::userToResponse);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable String id) {
        return userService.deleteById(id);
    }
}
