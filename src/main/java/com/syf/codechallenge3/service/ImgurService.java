package com.syf.codechallenge3.service;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syf.codechallenge3.config.ImgurConfig;
import com.syf.codechallenge3.model.ImageDto;

/**
 * Service class for interacting with the Imgur API.
 *
 * This service provides methods to upload and delete images from Imgur.
 * It uses the Apache HttpClient library to make HTTP requests to the Imgur API.
 *
 * Dependencies:
 * - ImgurConfig: Configuration properties for the Imgur API.
 *
 * Methods:
 * - deleteImage(String deleteHash): Deletes an image from Imgur using its
 * delete hash.
 * - uploadImage(ImageDto imageDto): Uploads an image to Imgur and returns the
 * image metadata.
 */
@Service
public class ImgurService {
    private final ImgurConfig imgurConfig;

    public ImgurService(ImgurConfig imgurConfig) {
        this.imgurConfig = imgurConfig;
    }

    /**
     * Deletes an image from Imgur using its delete hash.
     * 
     * @param deleteHash the delete hash of the image to be deleted
     * @throws IOException if the image delete fails throw an exception
     */
    public void deleteImage(String deleteHash) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpDelete deleteRequest = new HttpDelete(imgurConfig.getImgurApiImageBaseUrl() + "/" + deleteHash);
            deleteRequest.setHeader("Authorization", "Client-ID " + imgurConfig.getClientId());

            try (CloseableHttpResponse response = httpClient.execute(deleteRequest)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IOException("Failed to delete image from Imgur");
                }
            }
        }
    }

    /**
     * Uploads an image to Imgur.
     * 
     * @param imageDto the image data to be uploaded
     * @return the ImageDto object with Imgur metadata
     * @throws IOException if the image upload fails throw an exception
     */
    public ImageDto uploadImage(ImageDto imageDto) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(imgurConfig.getImgurApiImageBaseUrl());
            postRequest.setHeader("Authorization", "Client-ID " + imgurConfig.getClientId());
            postRequest.setHeader("Client-Secret", imgurConfig.getClientSecret());

            // Add image data to request
            MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
            meBuilder.addBinaryBody("image", new ByteArrayInputStream(imageDto.getImageData()),
                    ContentType.APPLICATION_OCTET_STREAM, imageDto.getFileName());
            postRequest.setEntity(meBuilder.build());

            // Add other metadata to request
            if (imageDto.getTitle() != null && !imageDto.getTitle().isEmpty()) {
                meBuilder.addTextBody("title", imageDto.getTitle(), ContentType.TEXT_PLAIN);
            }

            if (imageDto.getDescription() != null && !imageDto.getDescription().isEmpty()) {
                meBuilder.addTextBody("description", imageDto.getDescription(), ContentType.TEXT_PLAIN);
            }

            HttpEntity entity = meBuilder.build();
            postRequest.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
                HttpEntity responseEntity = response.getEntity();

                if (response.getStatusLine().getStatusCode() == 200) {
                    String responseString = EntityUtils.toString(responseEntity);
                    JsonNode jsonResponse = new ObjectMapper().readTree(responseString);
                    JsonNode data = jsonResponse.get("data");

                    imageDto.setImgurLink(data.get("link").asText());
                    imageDto.setImgurDeleteHash(data.get("deletehash").asText());
                    imageDto.setImgurId(data.get("id").asText());

                } else {
                    throw new IOException("Failed to upload image to Imgur");
                }
            }
        }

        return imageDto;
    }
}
