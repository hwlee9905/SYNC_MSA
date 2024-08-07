package project.service.security;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;
    private String nickname;
    private String position;
    private String introduction;
    private Role role;
}
