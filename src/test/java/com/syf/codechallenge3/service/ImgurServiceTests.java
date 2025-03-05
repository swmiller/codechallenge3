package com.syf.codechallenge3.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.http.HttpVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.http.HttpEntity;

import com.syf.codechallenge3.config.ImgurConfig;
import com.syf.codechallenge3.model.ImageDto;

public class ImgurServiceTests {

    @Mock
    private ImgurConfig imgurConfig;

    @InjectMocks
    private ImgurService imgurService;

    // Setup
    @BeforeEach
    public void setUp() {
        when(imgurConfig.getClientId()).thenReturn("test-client-id");
        when(imgurConfig.getClientSecret()).thenReturn("test-client-secret");
        when(imgurConfig.getImgurApiImageBaseUrl()).thenReturn("https://api.imgur.com/3/image");
    }

    @Test
    public void testUploadImage_Success() throws Exception {
        // Arrange
        ImageDto imageDto = GetTestImageDto();
        String jsonResponse = "{\"data\":{\"link\":\"http://imgur.com/test\",\"deletehash\":\"deletehash123\",\"id\":\"id123\"}}";

        MockedStatic<HttpClients> mockedStatic = mockStatic(HttpClients.class);
        CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
        CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);
        var mockEntity = mock(HttpEntity.class);

        mockedStatic.when(HttpClients::createDefault).thenReturn(mockHttpClient);
        when(mockHttpClient.execute(any(HttpPost.class))).thenReturn(mockResponse);
        when(mockResponse.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "OK"));
        when(mockResponse.getEntity()).thenReturn((org.apache.http.HttpEntity) mockEntity);
        when(EntityUtils.toString((org.apache.http.HttpEntity) mockEntity)).thenReturn(jsonResponse);

        // Act
        ImageDto result = imgurService.uploadImage(imageDto);

        // Assert
        assertNotNull(result);
        assertEquals("https://i.imgur.com/testimage.jpg", result.getImgurLink());
        assertEquals("abc123", result.getImgurDeleteHash());
        assertEquals("testImageId", result.getImgurId());
    }

    @Test
    public void testUploadImage_Failure() throws Exception {
        // Arrange
        ImageDto imageDto = GetTestImageDto();
        MockedStatic<HttpClients> mockedStatic = mockStatic(HttpClients.class);
        CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
        CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);

        mockedStatic.when(HttpClients::createDefault).thenReturn(mockHttpClient);
        when(mockHttpClient.execute(any(HttpPost.class))).thenReturn(mockResponse);
        when(mockResponse.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, 400, "Bad Request"));

        // Act / Assert
        assertThrows(IOException.class, () -> {
            imgurService.uploadImage(imageDto);
        });
    }

    @Test
    public void testDeleteImage_Success() throws Exception {
        // Arrange
        MockedStatic<HttpClients> mockedStatic = mockStatic(HttpClients.class);
        CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
        CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);

        mockedStatic.when(HttpClients::createDefault).thenReturn(mockHttpClient);
        when(mockHttpClient.execute(any(HttpDelete.class))).thenReturn(mockResponse);
        when(mockResponse.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "OK"));

        // Act / Assert
        assertDoesNotThrow(() -> {
            imgurService.deleteImage("deletehash123");
        });
    }

    @Test
    public void testDeleteImage_Failure() throws Exception {
        // Arrange
        MockedStatic<HttpClients> mockedStatic = mockStatic(HttpClients.class);
        CloseableHttpClient mockHttpClient = mock(CloseableHttpClient.class);
        CloseableHttpResponse mockResponse = mock(CloseableHttpResponse.class);

        mockedStatic.when(HttpClients::createDefault).thenReturn(mockHttpClient);
        when(mockHttpClient.execute(any(HttpDelete.class))).thenReturn(mockResponse);
        when(mockResponse.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, 400, "Bad Request"));

        // Act / Assert
        assertThrows(IOException.class, () -> {
            imgurService.deleteImage("deletehash123");
        });
    }

    private ImageDto GetTestImageDto() {
        ImageDto imageDto = new ImageDto() {
            {
                setImageData(new byte[] { 1, 2, 3, 4, 5 });
                setFileName("test.jpg");
                setTitle("Test Image");
                setDescription("This is a test image");
            }
        };

        return imageDto;
    }
}
