package com.syf.codechallenge3.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.syf.codechallenge3.exception.UserAlreadyExistsException;
import com.syf.codechallenge3.exception.UserNotFoundException;
import com.syf.codechallenge3.model.Image;
import com.syf.codechallenge3.model.User;
import com.syf.codechallenge3.service.UserService;

public class UserControllerTests {
    private static final String TEST_USERNAME = "Test Username";
    private static final long TEST_ID = 1L;
    private static final String USER_NOT_FOUND_EXCEPTION_MESSAGE = "User not found";
    private static final String USER_ALREADY_EXISTS_EXCEPTION_MESSAGE = "User already exists";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test controller endpoints

    @Test
    public void testRegisterUser() {
        // Arrange
        User testUser = getTestUser();
        when(userService.registerUser(any(User.class))).thenReturn(testUser);

        // Act
        ResponseEntity<User> response = userController.registerUser(testUser);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    public void testGetUserByUsername() {
        // Arrange
        User testUser = getTestUser();
        when(userService.getUserByUsername(anyString())).thenReturn(testUser);

        // Act
        ResponseEntity<User> response = userController.getUserByName(TEST_USERNAME);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    public void testGetUserById() {
        // Arrange
        User testUser = getTestUser();
        when(userService.getUserById(anyLong())).thenReturn(testUser);

        // Act
        ResponseEntity<User> response = userController.getUserById(TEST_ID);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    // Test exception handling

    @Test
    public void testHandleUserNotFoundEx() {
        // Arrange
        UserNotFoundException exception = new UserNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE);

        // Act
        ResponseEntity<String> response = userController.handleUserNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(USER_NOT_FOUND_EXCEPTION_MESSAGE, response.getBody());
    }

    @Test
    public void testHandleUserAlreadyExistsEx() {
        // Arrange
        UserAlreadyExistsException exception = new UserAlreadyExistsException(USER_ALREADY_EXISTS_EXCEPTION_MESSAGE);

        // Act
        ResponseEntity<String> response = userController.handleUserAlreadyExistsException(exception);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(USER_ALREADY_EXISTS_EXCEPTION_MESSAGE, response.getBody());
    }

    // Unhandled exception test

    @Test
    public void testHandleEx() {
        // Arrange
        Exception exception = new Exception(INTERNAL_SERVER_ERROR_MESSAGE);

        // Act
        ResponseEntity<String> response = userController.handleException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(INTERNAL_SERVER_ERROR_MESSAGE, response.getBody());
    }

    private User getTestUser() {
        User testUser2 = new User(TEST_ID, TEST_USERNAME, "testpassword", "testusername@someaddress.com", "Test",
                "Username", new ArrayList<Image>());
        return testUser2;
    }
}
