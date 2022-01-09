package gram.gs.server.impl.javalin.statics;

import gram.gs.exceptions.AuthenticationException;
import gram.gs.exceptions.ScoreApplicationException;
import gram.gs.exceptions.ScoreNotFoundException;

import java.util.UUID;

public class ServerExceptionHandler {

    private final static String SERVER_ERROR_ID = "SERVER_ERROR";

    private final static int INTERNAL_SERVER_ERROR_HTTP_STATUS = 500;
    private final static int NOT_FOUND_HTTP_STATUS = 404;
    private final static int UNAUTHORIZED_HTTP_STATUS = 401;
    private final static int BAD_REQUEST_HTTP_STATUS = 400;

    public static ServerExceptionResponse handle(Exception e) {
        if(e instanceof ScoreApplicationException) {
            ScoreApplicationException exception = (ScoreApplicationException) e;
            ServerExceptionResponse response = new ServerExceptionResponse();
            response.body(getBody(exception));
            if(e instanceof AuthenticationException) {
                return response.status(UNAUTHORIZED_HTTP_STATUS);
            }
            if(e instanceof ScoreNotFoundException) {
                return response.status(NOT_FOUND_HTTP_STATUS);
            }
            return response.status(BAD_REQUEST_HTTP_STATUS);
        } else {
            return new ServerExceptionResponse()
                    .body(getInternalServerExceptionBody())
                    .status(INTERNAL_SERVER_ERROR_HTTP_STATUS);
        }
    }

    private static ServerExceptionResponseBody getInternalServerExceptionBody() {
        return new ServerExceptionResponseBody()
                .id(SERVER_ERROR_ID)
                .message("an unknown error occurs")
                .traceId(UUID.randomUUID().toString());
    }

    private static ServerExceptionResponseBody getBody(ScoreApplicationException e) {
        return new ServerExceptionResponseBody()
                .id(e.getId())
                .message(e.getMessage())
                .traceId(UUID.randomUUID().toString());
    }
}
