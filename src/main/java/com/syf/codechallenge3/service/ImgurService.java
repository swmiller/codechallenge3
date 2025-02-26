package com.syf.codechallenge3.service;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;

import com.syf.codechallenge3.config.ImgurConfig;
import com.syf.codechallenge3.model.ImageDto;

@Service
public class ImgurService {
    private final ImgurConfig imgurConfig;

    public ImgurService(ImgurConfig imgurConfig) {
        this.imgurConfig = imgurConfig;
    }

    public ImageDto uploadImage(ImageDto imageDto) {
        return new ImageDto();
    }

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

    public ImageDto getImage(String imageId) {
        return new ImageDto();
    }
}
