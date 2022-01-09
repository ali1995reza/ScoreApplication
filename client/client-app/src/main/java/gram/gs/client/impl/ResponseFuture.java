package gram.gs.client.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

final class ResponseFuture<T> extends CompletableFuture<T> implements FutureCallback<HttpResponse> {

    public static <T> ResponseFuture<T> of(TypeReference<T> reference) {
        return new ResponseFuture<>(reference);
    }

    private final static ObjectMapper MAPPER = new ObjectMapper();


    private final TypeReference<T> clazz;

    public ResponseFuture(TypeReference<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void completed(HttpResponse httpResponse) {
        int status = httpResponse.getStatusLine().getStatusCode();
        if (status == 200) {
            try {
                T result = MAPPER.readValue(httpResponse.getEntity().getContent(), clazz);
                complete(result);
            } catch (Exception e) {
                completeExceptionally(e);
            }
        } else {
            try {
                completeExceptionally(MAPPER.readValue(httpResponse.getEntity().getContent(), ScoreServerError.class));
            } catch (IOException e) {
                completeExceptionally(new ScoreServerError().message("http request failed with status code : " + status).id("--").traceId("--"));
            }
        }
    }

    @Override
    public final void failed(Exception e) {
        completeExceptionally(e);
    }

    @Override
    public final void cancelled() {
        completeExceptionally(new IllegalStateException("http request canceled"));
    }
}
