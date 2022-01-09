package gram.gs.server.impl.javalin.statics;

public class ServerExceptionResponseBody {

    private String id;
    private String message;
    private String traceId;

    public ServerExceptionResponseBody(String id, String message, String traceId) {
        this.id = id;
        this.message = message;
        this.traceId = traceId;
    }

    public ServerExceptionResponseBody() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ServerExceptionResponseBody id(String id) {
        this.id = id;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ServerExceptionResponseBody message(String message) {
        this.message = message;
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public ServerExceptionResponseBody traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
}
