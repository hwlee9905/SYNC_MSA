package com.simple.book.domain.jwt.filter;

import com.simple.book.domain.jwt.dto.AuthTokenDto;
import com.simple.book.domain.jwt.dto.CustomUserDetails;
import com.simple.book.domain.jwt.util.JWTUtil;
import com.simple.book.domain.oauth2.CustomOAuth2User;
import com.simple.book.domain.oauth2.dto.OAuth2UserDto;
import com.simple.book.domain.user.util.InfoSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        //request에서 Authorization 헤더를 찾음
        Cookie[] cookies = request.getCookies();
        String jwtToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }
        if (jwtToken == null) {
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = jwtToken;

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            log.info("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 username과 role 획득
        String userId = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        String infoSet = jwtUtil.getInfoSet(token);
        String name = jwtUtil.getName(token);
        //userEntity를 생성하여 값 set
        AuthTokenDto user = AuthTokenDto.builder()
                .infoSet(infoSet)
                .username(userId)
                .name(name)
                .role(role)
                .password("temppassword")
                .build();
        OAuth2UserDto OAuth2user = OAuth2UserDto.builder()
                .infoSet(infoSet)
                .username(userId)
                .role(role)
                .name(name)
                .build();



        if(Objects.equals(user.getInfoSet(), InfoSet.DEFAULT.toString())){
            //UserDetails에 회원 정보 객체 담기
            CustomUserDetails customUserDetails = new CustomUserDetails(user);
            //스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        }else{

            //UserDetails에 회원 정보 객체 담기
            CustomOAuth2User customOAuth2User = new CustomOAuth2User(OAuth2user);

            //스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new OAuth2AuthenticationToken(customOAuth2User, customOAuth2User.getAuthorities(), customOAuth2User.getInfoSet());
            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        }



    }
}