package com.syf.codechallenge3.model;

import jakarta.persistence.*;

@Entity
public class Image {

    // Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String imgurDeleteHash;

    @Column(nullable = true)
    private String imgurLink;

    @Column(nullable = true)
    private String imgurId;

    // Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_profile_id", nullable = false)
    private User userProfile;

    // Constructors
    public Image() {
    }

    // Getters and Setters
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(User userProfile) {
        this.userProfile = userProfile;
    }

    // Methods
    public ImageDto toImageDto() {
        ImageDto imageDto = new ImageDto();
        imageDto.setTitle(this.title);
        imageDto.setDescription(this.description);
        imageDto.setImgurDeleteHash(this.imgurDeleteHash);
        imageDto.setImgurLink(this.imgurLink);
        imageDto.setImgurId(this.imgurId);

        return imageDto;
    }
}
