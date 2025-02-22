package com.syf.codechallenge3.service;

import org.springframework.stereotype.Service;

import com.syf.codechallenge3.config.ImgurConfig;
import com.syf.codechallenge3.model.ImageDto;
import com.syf.codechallenge3.repository.ImageRepository;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImgurConfig imgurConfig;
    private final String imgurClientId;
    private final String imgurClientSecret;
    private final String imgurApiImageBaseUrl;

    public ImageService(ImageRepository imageRepository, ImgurConfig imgurConfig) {
        this.imageRepository = imageRepository;
        this.imgurConfig = imgurConfig;

        this.imgurClientId = imgurConfig.getClientId();
        this.imgurClientSecret = imgurConfig.getClientSecret();
        this.imgurApiImageBaseUrl = imgurConfig.getImgurApiImageBaseUrl();
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
