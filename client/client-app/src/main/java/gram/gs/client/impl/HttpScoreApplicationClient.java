package gram.gs.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gram.gs.client.abs.ScoreApplicationClient;
import gram.gs.client.abs.dto.ClientToken;
import gram.gs.client.abs.dto.RankedScore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.nio.client.HttpAsyncClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HttpScoreApplicationClient implements ScoreApplicationClient {

    private final static ObjectMapper MAPPER = new ObjectMapper();
    private final static TypeReference<ClientToken> CLIENT_TOKEN_TYPE = SimpleTypeReference.of(ClientToken.class);
    private final static TypeReference<RankedScore> RANKED_SCORE_TYPE = SimpleTypeReference.of(RankedScore.class);
    private final static TypeReference<List<RankedScore>> RANK_LIST_TYPE = new TypeReference<List<RankedScore>>() {
    };

    private final HttpAsyncClient client;

    public HttpScoreApplicationClient(HttpAsyncClient client) {
        this.client = client;
    }


    @Override
    public CompletableFuture<ClientToken> login(String userId) {
        ResponseFuture<ClientToken> future = ResponseFuture.of(CLIENT_TOKEN_TYPE);
        try {
            client.execute(loginRequest(userId), future);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    @Override
    public CompletableFuture<RankedScore> submitScore(String token, String applicationId, long score) {
        ResponseFuture<RankedScore> future = ResponseFuture.of(RANKED_SCORE_TYPE);
        try {
            client.execute(submitRequest(token, applicationId, score), future);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    @Override
    public CompletableFuture<List<RankedScore>> getTopScoreList(String applicationId, long offset, long size) {
        ResponseFuture<List<RankedScore>> future = ResponseFuture.of(RANK_LIST_TYPE);
        try {
            client.execute(getTopScoreRequest(applicationId, offset, size), future);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    @Override
    public CompletableFuture<List<RankedScore>> searchScoreList(String userId, String applicationId, int top, int bottom) {
        ResponseFuture<List<RankedScore>> future = ResponseFuture.of(RANK_LIST_TYPE);
        try {
            client.execute(searchScoreRequest(userId, applicationId, top, bottom), future);
        } catch (Throwable e) {
            e.printStackTrace();
            future.completeExceptionally(e);
        }
        return future;
    }


    private static HttpUriRequest loginRequest(String userId) {
        return new HttpGet("http://localhost:8080/login/" + userId);
    }

    private static HttpUriRequest submitRequest(String token, String applicationId, long score) {
        HttpPut put = new HttpPut("http://localhost:8080/" + applicationId + "/scores");
        put.addHeader("X-CLIENT-TOKEN", token);
        try {
            put.setEntity(new ByteArrayEntity(MAPPER.writeValueAsBytes(new SubmitScoreRequestBody(score))));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
        return put;
    }

    private static HttpUriRequest getTopScoreRequest(String applicationId, long offset, long size) throws URISyntaxException {
        URI uri = new URIBuilder()
                .setHost("localhost")
                .setPort(8080)
                .setPath(applicationId + "/scores")
                .setParameter("offset", String.valueOf(offset))
                .setParameter("size", String.valueOf(size))
                .build();
        return new HttpGet(uri);
    }

    private static HttpUriRequest searchScoreRequest(String userId, String applicationId, int top, int bottom) throws URISyntaxException {
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("localhost")
                .setPort(8080)
                .setPath("applications/" + applicationId + "/scores/search")
                .setParameter("userId", userId)
                .setParameter("top", String.valueOf(top))
                .setParameter("bottom", String.valueOf(bottom))
                .setHost("localhost")
                .build();
        System.out.println(uri);
        return new HttpGet(uri);
    }
}
