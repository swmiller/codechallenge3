package com.syf.codechallenge3.service;

import org.springframework.stereotype.Service;

import com.syf.codechallenge3.exception.UserAlreadyExistsException;
import com.syf.codechallenge3.exception.UserNotFoundException;
import com.syf.codechallenge3.model.User;
import com.syf.codechallenge3.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User GetUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
    }

    public User GetUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public User RegisterUser(User user) {
        if (userRepository.findByUsername(user.getUserName()).isPresent()) {
            throw new UserAlreadyExistsException("User with username " + user.getUserName() + " already exists");
        }

        return userRepository.save(user);
    }
}
