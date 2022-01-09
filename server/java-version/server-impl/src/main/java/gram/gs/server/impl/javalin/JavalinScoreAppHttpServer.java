package gram.gs.server.impl.javalin;

import gram.gs.ScoreApplication;
import gram.gs.server.abs.ScoreAppHttpServer;
import gram.gs.server.impl.dto.SubmitScoreRequest;
import gram.gs.server.impl.dto.TokenResponse;
import gram.gs.server.impl.javalin.statics.ServerExceptionHandler;
import gram.gs.server.impl.javalin.statics.ServerExceptionResponse;
import gram.gs.server.impl.javalin.util.JsonUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;

import static gram.gs.server.impl.javalin.statics.ApiStatics.*;

public class JavalinScoreAppHttpServer extends ScoreAppHttpServer {

    private final static int JAVALIN_REQUIRED_THREADS = 3;

    private ScoreApplication application;

    @Override
    protected void doStart(final ScoreApplication application, final String host, final int port, final int numberOfHandlerThreads) {
        this.application = application;
        Javalin javalin = Javalin.create(config -> {
            config.server(() -> {
                Server server = new Server(new ExecutorThreadPool(numberOfHandlerThreads + JAVALIN_REQUIRED_THREADS));
                return server;
            });
        });
        javalin.addHandler(HandlerType.GET, Urls.LOGIN, this::login);
        javalin.addHandler(HandlerType.PUT, Urls.SUBMIT_SCORE, this::submitScore);
        javalin.addHandler(HandlerType.GET, Urls.GET_TOP_SCORES_LIST, this::getTopScoreList);
        javalin.addHandler(HandlerType.GET, Urls.SEARCH_SCORES_LIST, this::searchScoreList);
        javalin.exception(Exception.class, this::handleException);
        javalin.start(host, port);
    }

    private void login(Context ctx) throws Exception {
        String userId = ctx.pathParam(PathParams.USER_ID);
        ctx.result(JsonUtil.toJsonByte(new TokenResponse(
                application.login(userId)
        )));
    }

    private void submitScore(Context ctx) throws Exception {
        SubmitScoreRequest request = ctx.bodyStreamAsClass(SubmitScoreRequest.class);
        String applicationId = ctx.pathParam(PathParams.APPLICATION_ID);
        ctx.result(JsonUtil.toJsonByte(application.submitScore(
                ctx.header(Headers.CLIENT_TOKEN),
                applicationId,
                request.getScore())
        ));
    }

    private void getTopScoreList(Context ctx) throws Exception {
        final long offset = Long.parseLong(ctx.queryParam(QueryParams.OFFSET));
        final long size = Long.parseLong(ctx.queryParam(QueryParams.SIZE));
        final String applicationId = ctx.pathParam(PathParams.APPLICATION_ID);
        ctx.result(
                JsonUtil.toJsonByte(application.getTopScoreList(applicationId, offset, size))
        );
    }

    private void searchScoreList(Context ctx) throws Exception {
        final int top = Integer.parseInt(ctx.queryParam(QueryParams.TOP));
        final int bottom = Integer.parseInt(ctx.queryParam(QueryParams.BOTTOM));
        final String userId = ctx.queryParam(QueryParams.USER_ID);
        final String applicationId = ctx.pathParam(PathParams.APPLICATION_ID);
        ctx.result(
                JsonUtil.toJsonByte(application.searchScoreList(userId, applicationId, top, bottom))
        );
    }

    private void handleException(Exception e, Context ctx) {
        ServerExceptionResponse response = ServerExceptionHandler.handle(e);
        ctx.status(response.getStatus());
        ctx.result(JsonUtil.toJsonByte(response.getBody()));
    }
}
