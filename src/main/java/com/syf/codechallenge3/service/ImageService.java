package com.syf.codechallenge3.service;

import org.springframework.stereotype.Service;

import com.syf.codechallenge3.model.ImageDto;
import com.syf.codechallenge3.repository.ImageRepository;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void deleteImageById(long id) {
        imageRepository.deleteById(id);
    }

    public ImageDto uploadImage(ImageDto imageDto) {
        // Save image logic
        return new ImageDto();
    }

    public ImageDto getImageById(long id) {
        return new ImageDto();
    }
}
