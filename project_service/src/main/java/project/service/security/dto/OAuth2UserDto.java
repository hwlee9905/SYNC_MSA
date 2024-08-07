package project.service.security.dto;

import lombok.*;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuth2UserDto {
    private String name;
    private String role;
    private String username;
    private String infoSet;
}
