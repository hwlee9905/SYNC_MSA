package user.service.global.advice;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import user.service.jwt.dto.CustomUserDetails;
import user.service.oauth2.CustomOAuth2User;

@Slf4j
@NoArgsConstructor
public class ThreadLocalLogTrace implements LogTrace{
    private ThreadLocal<TraceStatus> traceIdHolder = new ThreadLocal<>();
    @Override
    public TraceStatus begin(String message) {
        String loginId = getCurrentUserId();
        Long startTimeMs = System.currentTimeMillis();
        TraceStatus traceStatus = new TraceStatus(loginId, startTimeMs, message);
        traceIdHolder.set(traceStatus);
        log.info("[{}] {}", traceStatus.getId(), message);
        return traceStatus;
    }
    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }
    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }
    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        if (e == null) {
            log.info("[{}] {} time={}ms", status.getId(), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {} time={}ms ex={}", status.getId(), status.getMessage(), resultTimeMs, e.toString());
        }
        releaseTraceId();
    }
    private void releaseTraceId() {
        // Thread Local 리소스 해제
        traceIdHolder.remove();
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
}
