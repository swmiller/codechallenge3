package com.syf.codechallenge3.service;

import java.io.Closeable;
import java.net.http.HttpResponse;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ImgurConfig imgurConfig;
    private final String imgurClientId;
    private final String imgurClientSecret;
    private final String imgurApiImageBaseUrl;
    private final UserRepository userRepository;

    public ImageService(ImageRepository imageRepository, UserRepository userRepository, ImgurConfig imgurConfig) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.imgurConfig = imgurConfig;

        this.imgurClientId = imgurConfig.getClientId();
        this.imgurClientSecret = imgurConfig.getClientSecret();
        this.imgurApiImageBaseUrl = imgurConfig.getImgurApiImageBaseUrl();
    }

    public void deleteImageById(long id) {
        // Get image metadata from database
        // Throw exception if image not found
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image with id " + id + " not found"));
        // Delete image from Imgur
        
        // Delete image metadata from database
        imageRepository.deleteById(id);
    }

    public ImageDto uploadImage(ImageDto imageDto) {
        // Validate username and password
        validateCredentials(imageDto.getUsername(), imageDto.getPassword());

        // 2. Upload image to Imgur
        ImageDto uploadedImage = uploadImageToImgur(imageDto);

        // 3. Save image metadata to database
        Image imageToSave = uploadedImage.toImage();
        imageRepository.save(imageToSave);

        // 4. Return image metadata
        return uploadedImage;
    }

    public ImageDto getImageById(long id) {
        // Get image metadata from database
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Image with id " + id + " not found"));
        return image.toImageDto();
    }

    // Validate username and password (not the best way to do this)
    private void validateCredentials(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
        if (!password.equals(user.getPassword())) {
            throw new UserNotAuthorizedException("Invalid password for user " + username);
        }
    }

    // Upload image to Imgur
    private ImageDto uploadImageToImgur(ImageDto imageDto) {
        try {
            // Setup HTTP client and request.
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost request = new HttpPost(imgurApiImageBaseUrl);
            request.addHeader("Authorization", "Client-ID " + imgurClientId);
            request.addHeader("Client-Secret", imgurClientSecret);

            // Execute request and get response.
            HttpResponse response = (HttpResponse) httpClient.execute(request);
            String responseBody = response.body().toString();

            // Parse response and get Imgur properties.
            getImgurProperties(responseBody, imageDto);
            return imageDto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image to Imgur", e);
        }
    }

    private void getImgurProperties(String jsonBody, ImageDto imageDto)
            throws JsonMappingException, JsonProcessingException {
        // Parse JSON response from Imgur
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonBody);
        JsonNode data = root.get("data");

        // Set image URL and delete hash in ImageDto
        imageDto.setImgurLink(data.get("link").asText());
        imageDto.setImgurDeleteHash(data.get("deletehash").asText());
        imageDto.setImgurId(data.get("id").asText());
    }
}
