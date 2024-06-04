package com.simple.book.domain.user.dto.request;

import com.simple.book.domain.user.util.Sex;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class SignupRequestDto {
    private String username;
    private String nickname;
    @Size(min = 8, max = 16)
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String userId;
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "비밀번호는 8~16자 영문, 숫자, 특수문자를 사용하세요.")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email
    private String email;
}
