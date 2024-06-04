package com.simple.book.domain.user.util;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileImage {
    @Column(name = "image_url")
    private String imageUrl;
    private String imagePath;
    private String imageName;
}
