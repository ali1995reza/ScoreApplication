package gram.gs.server.impl.javalin.statics;

public class ServerExceptionResponse {

    private int status;
    private ServerExceptionResponseBody body;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ServerExceptionResponse status(int status) {
        this.status = status;
        return this;
    }

    public ServerExceptionResponseBody getBody() {
        return body;
    }

    public ServerExceptionResponse body(ServerExceptionResponseBody body) {
        this.body = body;
        return this;
    }
}
