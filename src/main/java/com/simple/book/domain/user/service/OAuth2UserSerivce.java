package com.simple.book.domain.user.service;

import com.simple.book.domain.oauth2.CustomOAuth2User;
import com.simple.book.domain.oauth2.dto.OAuth2UserDto;
import com.simple.book.domain.oauth2.dto.response.OAuth2GoogleResponseDto;
import com.simple.book.domain.oauth2.dto.response.OAuth2KakaoResponseDto;
import com.simple.book.domain.oauth2.dto.response.OAuth2NaverResponseDto;
import com.simple.book.domain.oauth2.dto.response.OAuth2Response;
import com.simple.book.domain.user.entity.Authentication;
import com.simple.book.domain.user.entity.User;
import com.simple.book.domain.user.repository.AuthenticationRepository;
import com.simple.book.domain.user.repository.UserRepository;
import com.simple.book.domain.user.util.InfoSet;
import com.simple.book.domain.user.util.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class OAuth2UserSerivce extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final AuthenticationRepository authenticationRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        InfoSet infoSet;
        log.info("OAuth2User : " + oAuth2User);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            infoSet = InfoSet.NAVER;
            oAuth2Response = new OAuth2NaverResponseDto(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {
            infoSet = InfoSet.GOOGLE;
            oAuth2Response = new OAuth2GoogleResponseDto(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("kakao")) {
            infoSet = InfoSet.KAKAO;
            oAuth2Response = new OAuth2KakaoResponseDto(oAuth2User.getAttributes());
        }
        else {
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String userId = oAuth2Response.getProvider()+"_"+oAuth2Response.getProviderId();
        Authentication existData = authenticationRepository.findByUserId(userId);

        if (existData == null) {
            Authentication authentication = Authentication.builder()
                    .userId(userId)
                    .email(oAuth2Response.getEmail())
                    .infoSet(infoSet)
                    .build();
            authenticationRepository.save(authentication);
            User user = User.builder()
                    .username(oAuth2Response.getName())
                    .role(Role.USER)
                    .build();
            user.setAuthentication(authentication);
            authentication.setUser(user);
            userRepository.save(user);

            OAuth2UserDto oAuth2UserDto = OAuth2UserDto.builder()
                    .infoSet(infoSet.toString())
                    .name(oAuth2Response.getName())
                    .username(userId)
                    .role(Role.USER.toString())
                    .build();

            return new CustomOAuth2User(oAuth2UserDto);
        }
        else {


            OAuth2UserDto oAuth2UserDto = OAuth2UserDto.builder()
                    .infoSet(infoSet.toString())
                    .username(existData.getUserId())
                    .name(oAuth2Response.getName())
                    .role(Role.USER.toString())
                    .build();

            return new CustomOAuth2User(oAuth2UserDto);
        }
    }
}
