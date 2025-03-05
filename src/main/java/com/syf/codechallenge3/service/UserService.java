package com.syf.codechallenge3.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.syf.codechallenge3.exception.UserAlreadyExistsException;
import com.syf.codechallenge3.exception.UserNotFoundException;
import com.syf.codechallenge3.model.User;
import com.syf.codechallenge3.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String username) {
        logger.info("Getting user by username: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
    }

    public User getUserById(long id) {
        logger.info("Getting user by ID: {}", id);

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public User registerUser(User user) {
        logger.info("Registering user: {}", user);

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User with username " + user.getUsername() + " already exists");
        }

        return userRepository.save(user);
    }
}
