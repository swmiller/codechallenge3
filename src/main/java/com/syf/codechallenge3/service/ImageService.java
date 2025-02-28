package com.syf.codechallenge3.service;

import java.io.IOException;
import org.springframework.stereotype.Service;

import com.syf.codechallenge3.config.ImgurConfig;
import com.syf.codechallenge3.exception.ImageNotFoundException;
import com.syf.codechallenge3.exception.UserNotAuthorizedException;
import com.syf.codechallenge3.exception.UserNotFoundException;
import com.syf.codechallenge3.model.Image;
import com.syf.codechallenge3.model.ImageDto;
import com.syf.codechallenge3.model.User;
import com.syf.codechallenge3.repository.ImageRepository;
import com.syf.codechallenge3.repository.UserRepository;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ImgurService imgurService;

    public ImageService(ImageRepository imageRepository, UserRepository userRepository, ImgurConfig imgurConfig,
            ImgurService imgurService) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.imgurService = imgurService;
    }

    /**
     * Deletes an image by its ID.
     *
     * This method retrieves the image metadata from the database using the provided
     * ID.
     * 
     * If the image is found, it deletes the image from Imgur using the image's
     * delete hash.
     * If the image is not found, an ImageNotFoundException is thrown.
     *
     * @param id the ID of the image to be deleted
     * @throws IOException
     * @throws ImageNotFoundException if the image with the specified ID is not
     *                                found
     */
    public void deleteImageById(long id) throws IOException {
        // Get image metadata from database
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image with id " + id + " not found"));

        // Delete image from Imgur
        imgurService.deleteImage(image.getImgurDeleteHash());

        // Delete image metadata from database
        imageRepository.deleteById(id);
    }

    /***
     * Uploads an image to Imgur and saves the image metadata to the database.
     * 
     * @param imageDto the DTO containing the image data
     * @return The origianl image DTO with the Imgur link, delete hash, and ID added
     * @throws UserNotFoundException      if the user is not found
     * @throws UserNotAuthorizedException if the user is not authorized
     */
    public ImageDto uploadImage(ImageDto imageDto)
            throws UserNotFoundException, UserNotAuthorizedException, IOException {
        // Validate that user exists
        User user = userRepository.findByUsername(imageDto.getUsername())
                .orElseThrow(
                        () -> new UserNotFoundException("User with username " + imageDto.getUsername() + " not found"));

        // Validate that user credentials are authorized
        if (!user.getPassword().equals(imageDto.getPassword())) {
            throw new UserNotAuthorizedException("Invalid password for user " + imageDto.getUsername());
        }

        // Upload image to Imgur
        ImageDto uploadedImage = imgurService.uploadImage(imageDto);

        // Save image metadata to database
        imageRepository.save(uploadedImage.toImage());

        // Return image metadata
        return uploadedImage;
    }

    public ImageDto getImageById(long id) throws ImageNotFoundException {
        // Get image metadata from database
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image with id " + id + " not found"));

        return image.toImageDto();
    }

}
