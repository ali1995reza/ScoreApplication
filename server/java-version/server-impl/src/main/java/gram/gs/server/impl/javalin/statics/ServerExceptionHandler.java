package gram.gs.server.impl.javalin.statics;

import gram.gs.exceptions.AuthenticationException;
import gram.gs.exceptions.ScoreApplicationException;
import gram.gs.exceptions.ScoreNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ServerExceptionHandler {

    public final static Logger LOGGER = LoggerFactory.getLogger(ServerExceptionHandler.class);

    private final static String SERVER_ERROR_ID = "SERVER_ERROR";

    private final static int INTERNAL_SERVER_ERROR_HTTP_STATUS = 500;
    private final static int NOT_FOUND_HTTP_STATUS = 404;
    private final static int UNAUTHORIZED_HTTP_STATUS = 401;
    private final static int BAD_REQUEST_HTTP_STATUS = 400;

    public static ServerExceptionResponse handle(Exception e) {
        if (e instanceof ScoreApplicationException) {
            ServerExceptionResponse response = new ServerExceptionResponse();
            ScoreApplicationException exception = (ScoreApplicationException) e;
            response.body(getBody(exception));
            if (e instanceof AuthenticationException) {
                response.status(UNAUTHORIZED_HTTP_STATUS);
            }
            if (e instanceof ScoreNotFoundException) {
                response.status(NOT_FOUND_HTTP_STATUS);
            }
            response.status(BAD_REQUEST_HTTP_STATUS);
            LOGGER.warn("[{}] : \r\n {}", response.getBody().getId(), getStackTrace(e));
            return response;
        } else {
            ServerExceptionResponse response = new ServerExceptionResponse();
            response.body(getInternalServerExceptionBody())
                    .status(INTERNAL_SERVER_ERROR_HTTP_STATUS);
            LOGGER.error("[{}] : \r\n {}", response.getBody().getId(), getStackTrace(e));
            return response;
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

    private static String getStackTrace(Throwable e) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        e.printStackTrace(printStream);
        printStream.close();
        try {
            outputStream.close();
        } catch (IOException ioException) {
        }
        return outputStream.toString(StandardCharsets.UTF_8);
    }
}
