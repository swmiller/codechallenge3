package com.syf.codechallenge3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.syf.codechallenge3.exception.ImageNotFoundException;
import com.syf.codechallenge3.exception.UserNotAuthorizedException;
import com.syf.codechallenge3.exception.UserNotFoundException;
import com.syf.codechallenge3.model.Image;
import com.syf.codechallenge3.model.ImageDto;
import com.syf.codechallenge3.model.User;
import com.syf.codechallenge3.repository.ImageRepository;
import com.syf.codechallenge3.repository.UserRepository;

public class ImageServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImgurService imgurService;

    @InjectMocks
    private ImageService imageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test service methods

    // Note to self: use "verify" for mocks and "assert" for returned results.

    @Test
    public void testDeleteImageById_ImageExists() throws IOException {
        // Arrange
        Image testImage = new Image() {
            {
                setId(1L);
                setImgurDeleteHash("deleteHash");
            }
        };

        when(imageRepository.findById(testImage.getId())).thenReturn(Optional.of(testImage));

        // Act
        imageService.deleteImageById(testImage.getId());

        // Assert
        verify(imageRepository).deleteById(testImage.getId());
        verify(imgurService).deleteImage(testImage.getImgurDeleteHash());
    }

    @Test
    public void testDeleteImageById_ImageNotFound() throws IOException {
        // Arrange
        long testId = 1L;
        when(imageRepository.findById(testId)).thenReturn(Optional.empty());

        // Act
        assertThrows(ImageNotFoundException.class, () -> {
            imageService.deleteImageById(testId);
        });

        // Assert
        verify(imgurService, never()).deleteImage(anyString());
        verify(imageRepository, never()).deleteById(testId);
    }

    @Test
    public void testUploadImage_UserExistsAndAuthorized() throws IOException {
        // Arrange
        ImageDto testImageDto = new ImageDto() {
            {
                setUsername("user");
                setPassword("password");
            }
        };

        User user = new User() {
            {
                setUsername("user");
                setPassword("password");
            }
        };

        ImageDto uploadImageDto = new ImageDto();

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(imgurService.uploadImage(testImageDto)).thenReturn(uploadImageDto);

        // Act
        ImageDto result = imageService.uploadImage(testImageDto);

        // Assert
        assertNotNull(result);
        assertEquals(uploadImageDto, result);
        verify(imageRepository).save(any(Image.class));
    }

    @Test
    public void testUploadImage_UserNotFound() throws IOException {
        // Arrange
        ImageDto testImageDto = new ImageDto() {
            {
                setUsername("user");
                setPassword("password");
            }
        };

        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        // Act
        assertThrows(UserNotFoundException.class, () -> {
            imageService.uploadImage(testImageDto);
        });

        // Assert
        verifyNoInteractions(imgurService);
        verify(imageRepository, never()).save(any(Image.class));
    }

    @Test
    public void testUploadImage_UserNotAuthorized() throws IOException {
        // Arrange
        ImageDto testImageDto = new ImageDto() {
            {
                setUsername("user");
                setPassword("password");
            }
        };

        User user = new User() {
            {
                setUsername("user");
                setPassword("differentPassword");
            }
        };

        ImageDto uploadImageDto = new ImageDto();

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(imgurService.uploadImage(testImageDto)).thenReturn(uploadImageDto);

        // Act
        assertThrows(UserNotAuthorizedException.class, () -> {
            imageService.uploadImage(testImageDto);
        });

        // Assert
        verifyNoInteractions(imgurService);
        verify(imageRepository, never()).save(any(Image.class));
    }

    /*
     * public ImageDto getImageById(long id) throws ImageNotFoundException {
     * // Get image metadata from database
     * Image image = imageRepository.findById(id)
     * .orElseThrow(() -> new ImageNotFoundException("Image with id " + id +
     * " not found"));
     * 
     * return image.toImageDto();
     * }
     * 
     * 
     * 
     * 
     * 
     */
    @Test
    public void testGerImageById_ImageExists() {
        // Arrange
        Long imageId = 1L;
        Image testImage = new Image() {
            {
                setId(imageId);
            }
        };

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(testImage));

        // Act
        ImageDto result = imageService.getImageById(imageId);

        // Assert
        assertNotNull(result);
        assertEquals(imageId, result.getId());
    }

    @Test
    public void testGetImageById_ImageNotFound() {
        // Arrange
        Long imageId = 1L;
        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        // Act
        assertThrows(ImageNotFoundException.class, () -> {
            imageService.getImageById(imageId);
        });

        // Assert
        verify(imageRepository).findById(imageId);
    }
}
