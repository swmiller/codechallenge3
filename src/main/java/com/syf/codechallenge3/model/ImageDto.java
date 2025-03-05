package com.syf.codechallenge3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ImageDto {
    private Long id;
    private String fileName;
    private String fileType; // Image or video
    private byte[] imageData;
    private String username;
    private String password;
    private String imageFormat;
    private String videoFormat;
    private String title;
    private String description;
    private String imgurDeleteHash;
    private String imgurLink;
    private String imgurId;

    // Methods

    public Image toImage() {
        Image image = new Image();
        image.setTitle(this.title);
        image.setDescription(this.description);
        image.setImgurDeleteHash(this.imgurDeleteHash);
        return image;
    }
}
