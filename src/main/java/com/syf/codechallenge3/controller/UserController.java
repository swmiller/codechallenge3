package com.syf.codechallenge3.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syf.codechallenge3.exception.UserAlreadyExistsException;
import com.syf.codechallenge3.exception.UserNotFoundException;
import com.syf.codechallenge3.model.User;
import com.syf.codechallenge3.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller class for exposing user management endpoints.
 *
 * This controller provides endpoints to register and retrieve users.
 * It interacts with the database to store and retrieve basic user information.
 *
 * Dependencies:
 * - UserService: Service for managing user data.
 *
 * Endpoints:
 * - registerUser: Registers a new user in the system.
 * - getUserByName: Retrieves a user by their username.
 * - getUserById: Retrieves a user by their ID.
 */

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user in the system.
     * 
     * @param user the user data to be registered
     * @return the registered user data
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User newUser = userService.registerUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    /**
     * Retrieves a user by their username.
     * 
     * @param username the username of the user to be retrieved
     * @return the user data
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByName(@PathVariable("username") String username) {
        User newUser = userService.getUserByUsername(username);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    /**
     * Retrieves a user by their ID.
     * 
     * @param id the ID of the user to be retrieved
     * @return the user data
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        User newUser = userService.getUserById(id);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    /**
     * Exception handler for UserNotFoundException.
     * 
     * @param e the exception to be handled
     * @return a response entity with status NOT_FOUND
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Exception handler for UserAlreadyExistsException.
     * 
     * @param e the exception to be handled
     * @return a response entity with status CONFLICT
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Exception handler for generic exceptions.
     * 
     * @param e the exception to be handled
     * @return a response entity with status INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
