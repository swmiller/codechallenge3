package com.syf.codechallenge3.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syf.codechallenge3.exception.ImageNotFoundException;
import com.syf.codechallenge3.exception.UserNotAuthorizedException;
import com.syf.codechallenge3.exception.UserNotFoundException;
import com.syf.codechallenge3.model.ImageDto;
import com.syf.codechallenge3.service.ImageService;
import com.syf.codechallenge3.service.UserService;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controller class for exposing image management endpoints.
 *
 * This controller provides endpoints to upload, delete, and retrieve images.
 * It interacts with the database to store and retrieve image metadata,
 * and with the Imgur service to handle image uploads and deletions.
 *
 * Dependencies:
 * - UserService: Service for managing user data.
 * - ImageService: Service for managing images.
 *
 * Endpoints:
 * - uploadImage: Uploads an image to Imgur and saves its metadata to the
 * database.
 * - deleteImageById: Deletes and image from the dataabase and removes it from
 * the Imgur platform.
 * - uploadImage: Uploads an image to Imgur and saves its metadata to the
 * database.
 * - getImageById: Retrieves an image by its ID.
 */
@RestController
@RequestMapping("/api/images")
public class ImageController {

    private ImageService imageService;

    public ImageController(UserService userService, ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Uploads an image to Imgur and saves its metadata to the database.
     * 
     * @param imageDTO the image data to be uploaded
     * @return the uploaded image data
     * @throws IOException
     */
    @PostMapping("/upload")
    public ResponseEntity<ImageDto> uploadImage(@RequestBody ImageDto imageDTO) throws IOException {
        ImageDto uploadedImage = imageService.uploadImage(imageDTO);
        return new ResponseEntity<>(uploadedImage, HttpStatus.OK);
    }

    /**
     * Retrieves an image by its ID.
     * 
     * @param id the ID of the image to be retrieved
     * @return the image data
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<ImageDto> getImage(@PathVariable("id") Long id) {
        ImageDto image = imageService.getImageById(id);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    /**
     * Deletes an image by its ID.
     * 
     * @param id the ID of the image to be deleted
     * @return a response entity with status OK
     * @throws IOException
     */
    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable("id") Long id) throws IOException {
        imageService.deleteImageById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Handles ImageNotFoundException exceptions.
     * 
     * @param e the exception to be handled
     * @return a response entity with the exception message and status NOT_FOUND
     */
    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<String> handleImageNotFoundException(ImageNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles UserNotFoundException exceptions.
     * 
     * @param e the exception to be handled
     * @return a response entity with the exception message and status NOT_FOUND
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles UserNotAuthorizedException exceptions.
     * 
     * @param e the exception to be handled
     * @return a response entity with the exception message and status UNAUTHORIZED
     */
    @ExceptionHandler(UserNotAuthorizedException.class)
    public ResponseEntity<String> handleUserNotAuthorizedException(UserNotAuthorizedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles IOException exceptions.
     * 
     * @param e the exception to be handled
     * @return a response entity with the exception message and status
     *         INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles general exceptions.
     * 
     * @param e the exception to be handled
     * @return a response entity with the exception message and status
     *         INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
