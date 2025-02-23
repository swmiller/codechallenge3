package com.syf.codechallenge3.model;

public class ImageDto {
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

    // Getters and Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat;
    }

    public String getVideoFormat() {
        return videoFormat;
    }

    public void setVideoFormat(String videoFormat) {
        this.videoFormat = videoFormat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgurDeleteHash() {
        return imgurDeleteHash;
    }

    public void setImgurDeleteHash(String imgurImageHash) {
        this.imgurDeleteHash = imgurImageHash;
    }

    public String getImgurLink() {
        return imgurLink;
    }

    public void setImgurLink(String imgurLink) {
        this.imgurLink = imgurLink;
    }

    public String getImgurId() {
        return imgurId;
    }

    public void setImgurId(String imgurId) {
        this.imgurId = imgurId;
    }

    // Methods

    public Image toImage() {
        Image image = new Image();
        image.setTitle(this.title);
        image.setDescription(this.description);
        image.setImgurImageHash(this.imgurDeleteHash);
        return image;
    }
}
