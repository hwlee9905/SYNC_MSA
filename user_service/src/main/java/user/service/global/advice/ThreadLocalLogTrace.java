package user.service.global.advice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalLogTrace implements LogTrace{
    private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();
    @Override
    public TraceStatus begin(String message) {
        TraceId traceId = new TraceId();
        traceIdHolder.set(traceId);
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}", traceId.getId(), message);
        return new TraceStatus(traceId, startTimeMs, message);
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
        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {} time={}ms", traceId.getId(), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {} time={}ms ex={}", traceId.getId(), status.getMessage(), resultTimeMs, e.toString());
        }
        releaseTraceId();
    }
    private void releaseTraceId() {
        // Thread Local 리소스 해제
        traceIdHolder.remove();
    }
}
