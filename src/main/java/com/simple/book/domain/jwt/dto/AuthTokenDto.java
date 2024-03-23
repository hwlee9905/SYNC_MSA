package com.simple.book.domain.jwt.dto;

import com.simple.book.domain.user.util.InfoSet;
import lombok.*;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthTokenDto {
    String name;
    String username;
    String role;
    String password;
    String infoSet;

}
