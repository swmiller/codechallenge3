package com.syf.codechallenge3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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
