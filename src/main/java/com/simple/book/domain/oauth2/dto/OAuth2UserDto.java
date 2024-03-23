package com.simple.book.domain.oauth2.dto;

import com.simple.book.domain.user.util.InfoSet;
import lombok.*;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuth2UserDto {
    private String role;
    private String name;
    private String username;
    private String infoSet;
}
