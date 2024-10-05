package user.service.global.advice;

public class TraceStatus {
    private Long startTimeMs;
    private String message;
    private String id;
    public TraceStatus(String id, Long startTimeMs, String message) {
        this.id = id;
        this.startTimeMs = startTimeMs;
        this.message = message;
    }
    public Long getStartTimeMs() {
        return startTimeMs;
    }
    public String getMessage() {
        return message;
    }
    public String getId() {
        return id;
    }
}
