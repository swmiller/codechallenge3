package com.syf.codechallenge3.model;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import jakarta.persistence.*;

public class Image {

    // Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ImgurUrl;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile userProfile;

    // Constructors
    public Image() {
    }

    public Image(String ImgurUrl, UserProfile userProfile) {
        this.ImgurUrl = ImgurUrl;
        this.userProfile = userProfile;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgurUrl() {
        return ImgurUrl;
    }

    public void setImgurUrl(String ImgurUrl) {
        this.ImgurUrl = ImgurUrl;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
