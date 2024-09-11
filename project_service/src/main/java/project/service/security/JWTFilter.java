package project.service.security;

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
import org.springframework.web.servlet.HandlerExceptionResolver;
import project.service.security.dto.AuthTokenDto;
import project.service.security.dto.CustomUserDetails;
import project.service.security.dto.OAuth2UserDto;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver exceptionResolver;
    private final JWTUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        try {
            // cookie에서 jwt 토큰 추출
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
                return;
            }

            // 토큰 검증
            if (jwtUtil.isExpired(jwtToken)) {
                log.info("token expired");
                filterChain.doFilter(request, response);
                return;
            }

            // jwt에서 유저정보 추출
            String userId = jwtUtil.getUsername(jwtToken);
            String role = jwtUtil.getRole(jwtToken);
            String infoSet = jwtUtil.getInfoSet(jwtToken);
            String name = jwtUtil.getName(jwtToken);

            //userEntity를 생성하여 값 set
            AuthTokenDto user = AuthTokenDto.builder()
                    .infoSet(infoSet)
                    .username(userId)
                    .name(name)
                    .role(role)
                    .password("temppassword")
                    .build();
            OAuth2UserDto OAuth2user = OAuth2UserDto.builder()
                    .name(name)
                    .infoSet(infoSet)
                    .username(userId)
                    .role(role)
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
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
        }




    }
}