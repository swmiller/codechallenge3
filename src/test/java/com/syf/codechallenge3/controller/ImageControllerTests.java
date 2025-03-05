package com.syf.codechallenge3.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.syf.codechallenge3.exception.ImageNotFoundException;
import com.syf.codechallenge3.exception.UserNotAuthorizedException;
import com.syf.codechallenge3.exception.UserNotFoundException;
import com.syf.codechallenge3.model.ImageDto;
import com.syf.codechallenge3.repository.UserRepository;
import com.syf.codechallenge3.service.ImageService;
import com.syf.codechallenge3.service.UserService;

public class ImageControllerTests {
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final String USER_NOT_AUTHORIZED_MESSAGE = "User not authorized";
    private static final String IMAGE_NOT_FOUND_MESSAGE = "Image not found";
    private static final String EXCEPTION_MESSAGE = "Exception";

    @Mock
    private ImageService imageService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ImageController imageController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test controller endpoints

    @Test
    public void testUploadImage_Success() throws UserNotFoundException, UserNotAuthorizedException, IOException {
        // Arrange
        ImageDto testImage = createTestImageDto();
        when(imageService.uploadImage(any(ImageDto.class))).thenReturn(testImage);

        // Act
        ResponseEntity<ImageDto> response = imageController.uploadImage(testImage);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testImage, response.getBody());
    }

    public void testUploadImage_UserNotFoundException()
            throws UserNotFoundException, UserNotAuthorizedException, IOException {
        // Arrange
        ImageDto testImage = createTestImageDto();
        doThrow(new UserNotFoundException(USER_NOT_FOUND_MESSAGE)).when(imageService).uploadImage(any(ImageDto.class));

        // Act
        ResponseEntity<ImageDto> response = imageController.uploadImage(testImage);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(USER_NOT_FOUND_MESSAGE, response.getBody());
    }

    public void testUploadImage_UserNotAuthorizedException()
            throws UserNotFoundException, UserNotAuthorizedException, IOException {
        // Arrange
        ImageDto testImage = createTestImageDto();
        doThrow(new UserNotAuthorizedException(USER_NOT_AUTHORIZED_MESSAGE)).when(imageService)
                .uploadImage(any(ImageDto.class));

        // Act
        ResponseEntity<ImageDto> response = imageController.uploadImage(testImage);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(USER_NOT_AUTHORIZED_MESSAGE, response.getBody());
    }

    @Test
    public void testGetImage_Success() {
        // Arrange
        ImageDto testImage = createTestImageDto();
        when(imageService.getImageById(any(Long.class))).thenReturn(testImage);

        // Act
        ResponseEntity<ImageDto> response = imageController.getImage(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testImage, response.getBody());
    }

    @Test
    public void testGetImage_ImageNotFoundException() {
        // Arrange
        when(imageService.getImageById(any(Long.class))).thenThrow(new ImageNotFoundException(IMAGE_NOT_FOUND_MESSAGE));

        // Act and Assert
        assertThrows(ImageNotFoundException.class, () -> {
            imageController.getImage(2L);
        });
    }

    @Test
    public void testDeleteImage_Success() throws UserNotFoundException, UserNotAuthorizedException, IOException {
        // Arrange
        ImageDto testImage = createTestImageDto();
        when(imageService.uploadImage(any(ImageDto.class))).thenReturn(testImage);

        // Act
        ResponseEntity<ImageDto> response = imageController.uploadImage(testImage);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testImage, response.getBody());
    }

    @Test
    public void testDeleteImage_ImageNotFoundException()
            throws IOException, UserNotFoundException, UserNotAuthorizedException {
        // Arrange
        doThrow(new ImageNotFoundException(IMAGE_NOT_FOUND_MESSAGE)).when(imageService).deleteImageById(anyLong());

        // Act and Assert
        assertThrows(ImageNotFoundException.class, () -> {
            imageController.deleteImage(3L);
        });
    }

    @Test
    public void testHandleImageNotFoundException() {
        // Arrange
        ImageNotFoundException exception = new ImageNotFoundException(IMAGE_NOT_FOUND_MESSAGE);

        // Act
        ResponseEntity<String> response = imageController.handleImageNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(IMAGE_NOT_FOUND_MESSAGE, response.getBody());
    }

    @Test
    public void testHandleUserNotFoundException() {
        // Arrange
        UserNotFoundException exception = new UserNotFoundException(USER_NOT_FOUND_MESSAGE);

        // Act
        ResponseEntity<String> response = imageController.handleUserNotFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(USER_NOT_FOUND_MESSAGE, response.getBody());
    }

    @Test
    public void testHandleUserNotAuthorizedException() {
        // Arrange
        UserNotAuthorizedException exception = new UserNotAuthorizedException(USER_NOT_AUTHORIZED_MESSAGE);

        // Act
        ResponseEntity<String> response = imageController.handleUserNotAuthorizedException(exception);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(USER_NOT_AUTHORIZED_MESSAGE, response.getBody());
    }

    @Test
    public void testHandleException() {
        // Arrange
        Exception exception = new Exception(EXCEPTION_MESSAGE);

        // Act
        ResponseEntity<String> response = imageController.handleException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(EXCEPTION_MESSAGE, response.getBody());
    }

    private ImageDto createTestImageDto() {
        ImageDto imageDto = new ImageDto(
                1L,
                "testFileName.jpg",
                "Image",
                new byte[] { 0x00, 0x01, 0x02 },
                "testUsername",
                "testPassword",
                "JPEG",
                "MP4",
                "testTitle",
                "testDescription",
                "testDeleteHash",
                "testLink",
                "testId");

        return imageDto;
    }
}
