package com.syf.codechallenge3.model;

import jakarta.persistence.*;

@Entity
public class Image {

    // Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ImgurUrl;

    // Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_profile_id", nullable = false)
    private User userProfile;

    // Constructors
    public Image() {
    }

    public Image(String ImgurUrl, User userProfile) {
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

    public User getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(User userProfile) {
        this.userProfile = userProfile;
    }
}
