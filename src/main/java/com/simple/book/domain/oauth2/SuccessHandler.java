package com.simple.book.domain.oauth2;

import com.simple.book.domain.jwt.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
@Component
@RequiredArgsConstructor
public class SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        String name = customUserDetails.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();


        String token = jwtUtil.createJwt(username, role, 60*30*1000L);

        Cookie jwtCookie = createCookie("JWT_TOKEN",  token);
        String encodedName = URLEncoder.encode(name, "UTF-8");
        response.addCookie(jwtCookie);
        response.sendRedirect("https://localhost:9090/?username=" + username + "&role=" + role + "&name=" + encodedName);
    }

    //쿠키로 JWT 발급
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*30); // 30분 유효
        cookie.setSecure(true); // HTTPS에서만 전송
        cookie.setPath("/"); // 전체 경로에서 접근 가능
        cookie.setHttpOnly(true); // JavaScript에서 접근 불가
        return cookie;
    }
}