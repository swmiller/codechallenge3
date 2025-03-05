package com.syf.codechallenge3.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Service class for managing images.
 *
 * This service provides methods to upload, delete, and retrieve images.
 * It interacts with the database to store and retrieve image metadata,
 * and with the Imgur service to handle image uploads and deletions.
 *
 * Dependencies:
 * - ImageRepository: Repository for image metadata.
 * - UserRepository: Repository for user data.
 * - ImgurService: Service for interacting with Imgur API.
 *
 * Methods:
 * - deleteImageById(long id): Deletes an image by its ID.
 * - uploadImage(ImageDto imageDto): Uploads an image to Imgur and saves its
 * metadata.
 * - getImageById(long id): Retrieves an image by its ID.
 */
@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ImgurService imgurService;
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

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
        logger.info("Deleting image with id: {}", id);

        // Get image metadata from database
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image with id " + id + " not found"));

        // Delete image from Imgur
        imgurService.deleteImage(image.getImgurDeleteHash());

        // Delete image metadata from database
        imageRepository.deleteById(id);
    }

    /**
     * Uploads an image to Imgur and saves its metadata to the database.
     *
     * This method validates the user's existence and authorization before uploading
     * the image to Imgur. If the user is not found, a UserNotFoundException is
     * thrown.
     * If the user's credentials are not authorized, a UserNotAuthorizedException is
     * thrown.
     * After successful upload, the image metadata is saved to the database.
     *
     * @param imageDto the DTO containing image data and user credentials
     * @return the DTO containing the uploaded image metadata
     * @throws UserNotFoundException      if the user with the specified username is
     *                                    not found
     * @throws UserNotAuthorizedException if the user's credentials are not
     *                                    authorized
     * @throws IOException                if an I/O error occurs during the upload
     *                                    process
     */
    public ImageDto uploadImage(ImageDto imageDto)
            throws UserNotFoundException, UserNotAuthorizedException, IOException {
        logger.info("Uploading image: {}", imageDto);

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

    /**
     * Retrieves an image by its ID.
     *
     * This method fetches the image metadata from the database using the provided
     * ID.
     * If the image is found, it returns the image metadata as an ImageDto.
     * If the image is not found, an ImageNotFoundException is thrown.
     *
     * @param id the ID of the image to be retrieved
     * @return the DTO containing the image metadata
     * @throws ImageNotFoundException if the image with the specified ID is not
     *                                found
     */
    public ImageDto getImageById(long id) throws ImageNotFoundException {
        logger.info("Retrieving image by ID: {}", id);

        // Get image metadata from database
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image with id " + id + " not found"));

        return image.toImageDto();
    }
}
