package user.service.global.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AspectLogAdvice {
    private final LogTrace logTrace;
    private final ThreadLocal<TraceStatus> traceStatusThreadLocal = new ThreadLocal<>();
    @Before("@annotation(user.service.global.advice.LogAop)")
    public void doBefore(JoinPoint joinPoint) {
        String message = joinPoint.getSignature().toShortString();
        TraceStatus status = logTrace.begin(message);
        traceStatusThreadLocal.set(status);
    }
    @AfterReturning(value = "@annotation(user.service.global.advice.LogAop)", returning = "result")
    public void doAfterReturning(Object result) {
        TraceStatus status = traceStatusThreadLocal.get();
        logTrace.end(status);
        traceStatusThreadLocal.remove();
    }
    @AfterThrowing(value = "@annotation(user.service.global.advice.LogAop)", throwing = "e")
    public void doAfterThrowing(Exception e) {
        TraceStatus status = traceStatusThreadLocal.get();
        logTrace.exception(status, e);
        traceStatusThreadLocal.remove();
    }
}
