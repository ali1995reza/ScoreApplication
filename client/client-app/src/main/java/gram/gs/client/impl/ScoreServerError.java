package gram.gs.client.impl;

public class ScoreServerError extends RuntimeException {

    private String id;
    private String message;
    private String traceId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ScoreServerError id(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ScoreServerError message(String message) {
        this.message = message;
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public ScoreServerError traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    @Override
    public String getLocalizedMessage() {
        return "[id : " + id + ", message : " + getMessage() + ", traceId : " + traceId + "]";
    }
}
