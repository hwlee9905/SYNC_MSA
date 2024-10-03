package user.service.global.advice;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import user.service.jwt.dto.CustomUserDetails;
import user.service.oauth2.CustomOAuth2User;

public class TraceId {
    private String id;
    public TraceId() {
        this.id = getCurrentUserId();
    }
    public String getCurrentUserId() {
        org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication instanceof OAuth2AuthenticationToken) {
                CustomOAuth2User oauthToken = (CustomOAuth2User) authentication.getPrincipal();
                return oauthToken.getUsername(); // OAuth2로 인증된 경우 사용자 ID 추출
            } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                return customUserDetails.getUsername();
            }
        }
        return "Guest"; // 사용자가 인증되지 않았거나 인증 정보가 없는 경우
    }
    public String getId() {
        return id;
    }
}