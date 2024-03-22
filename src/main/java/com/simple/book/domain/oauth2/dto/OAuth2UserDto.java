package com.simple.book.domain.oauth2.dto;

import lombok.*;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuth2UserDto {
    private String role;
    private String name;
    private String username;
}
