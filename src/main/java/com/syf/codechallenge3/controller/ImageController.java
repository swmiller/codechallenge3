package com.syf.codechallenge3.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syf.codechallenge3.model.ImageDto;
import com.syf.codechallenge3.service.ImageService;
import com.syf.codechallenge3.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final UserService userService;
    private ImageService imageService;

    public ImageController(UserService userService, ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ImageDto> uploadImage(@RequestBody ImageDto imageDTO) {
        ImageDto uploadedImage = imageService.uploadImage(imageDTO);
        return new ResponseEntity<>(uploadedImage, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ImageDto> getImage(@PathVariable("id") Long id) {
        ImageDto image = imageService.getImageById(id);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable("id") Long id) {
        imageService.deleteImageById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
