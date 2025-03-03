package com.syf.codechallenge3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Imgur API.
 *
 * This class provides configuration properties for the Imgur API,
 * including the client ID, client secret, and base URL for image uploads.
 *
 * Properties:
 * - clientId: The client ID for the Imgur API.
 * - clientSecret: The client secret for the Imgur API.
 * - imgurApiImageBaseUrl: The base URL for uploading images to Imgur.
 */
@Configuration
public class ImgurConfig {

    @Value("${imgur.client.id}")
    private String clientId;

    @Value("${imgur.client.secret}")
    private String clientSecret;

    @Value("${imgur.api.image.base.url}")
    private String imgurApiImageBaseUrl;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getImgurApiImageBaseUrl() {
        return imgurApiImageBaseUrl;
    }
}
