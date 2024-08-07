package project.service.security.dto;

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
