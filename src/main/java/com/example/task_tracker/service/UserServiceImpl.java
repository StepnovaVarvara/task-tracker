package com.example.task_tracker.service;

import com.example.task_tracker.entity.User;
import com.example.task_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> findById(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Mono<User> save(User user) {
        user.setId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    @Override
    public Mono<User> update(String userId, User user) {
        return findById(userId).flatMap(userForUpdate -> {
            if (StringUtils.hasText(user.getUserName())) {
                userForUpdate.setUserName(user.getUserName());
            }

            if (StringUtils.hasText(user.getEmail())) {
                userForUpdate.setEmail(user.getEmail());
            }

            return userRepository.save(userForUpdate);
        });
    }

    @Override
    public Mono<Void> deleteById(String userId) {
        return userRepository.deleteById(userId);
    }
}
