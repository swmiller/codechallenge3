package com.syf.codechallenge3.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.syf.codechallenge3.model.ImageDto;
import com.syf.codechallenge3.service.ImageService;
import com.syf.codechallenge3.service.UserService;

public class ImageControllerTests {

    @Mock
    private ImageService imageService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ImageController imageController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test controller endpoints

    @Test
    public void testUploadImage() {
        // Arrange
        var testImage = createTestImageDto();
        when(imageService.uploadImage(any(ImageDto.class))).thenReturn(testImage);

        // Act
        var response = imageController.uploadImage(testImage);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testImage, response.getBody());
    }

    @Test
    public void testGetImage() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void testDeleteImage() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void testHandleImageNotFoundException() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void testHandleUserNotFoundException() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void testHandleUserNotAuthorizedException() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void testHandleException() {
        // Arrange
        // Act
        // Assert
    }

    private ImageDto createTestImageDto() {
        ImageDto imageDto = new ImageDto();
        imageDto.setFileName("testFileName.jpg");
        imageDto.setFileType("Image");
        imageDto.setImageData(new byte[] { 0x00, 0x01, 0x02 });
        imageDto.setUsername("testUsername");
        imageDto.setPassword("testPassword");
        imageDto.setImageFormat("JPEG");
        imageDto.setVideoFormat("MP4");
        imageDto.setTitle("testTitle");
        imageDto.setDescription("testDescription");
        imageDto.setImgurDeleteHash("testDeleteHash");
        imageDto.setImgurLink("testLink");
        imageDto.setImgurId("testId");

        return imageDto;
    }
}
