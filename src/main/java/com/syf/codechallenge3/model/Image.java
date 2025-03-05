package com.syf.codechallenge3.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    // Methods
    public ImageDto toImageDto() {
        ImageDto imageDto = new ImageDto();
        imageDto.setId(this.id);
        imageDto.setTitle(this.title);
        imageDto.setDescription(this.description);
        imageDto.setImgurDeleteHash(this.imgurDeleteHash);
        imageDto.setImgurLink(this.imgurLink);
        imageDto.setImgurId(this.imgurId);

        return imageDto;
    }
}
