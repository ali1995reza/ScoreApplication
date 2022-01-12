package gram.gs.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gram.gs.client.abs.ScoreApplicationClient;
import gram.gs.client.abs.dto.ClientToken;
import gram.gs.client.abs.dto.RankedScore;
import gram.gs.client.assertion.Assert;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HttpScoreApplicationClient implements ScoreApplicationClient {

    private final static ObjectMapper MAPPER = new ObjectMapper();
    private final static TypeReference<ClientToken> CLIENT_TOKEN_TYPE = SimpleTypeReference.of(ClientToken.class);
    private final static TypeReference<RankedScore> RANKED_SCORE_TYPE = SimpleTypeReference.of(RankedScore.class);
    private final static TypeReference<List<RankedScore>> RANK_LIST_TYPE = new TypeReference<>() {
    };

    private final CloseableHttpAsyncClient client;
    private final String host;
    private final int port;

    public HttpScoreApplicationClient(CloseableHttpAsyncClient client, String host, int port) {
        this.validate(host, port);
        this.client = client;
        this.host = host;
        this.port = port;
        if (!client.isRunning()) {
            client.start();
        }
    }

    public HttpScoreApplicationClient(String host, int port) {
        this(HttpAsyncClients.createDefault(), host, port);
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

    @Override
    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void validate(String host, int port) {
        try {
            new URIBuilder()
                    .setScheme("http")
                    .setHost(host)
                    .setPort(port)
                    .build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("invalid host name [" + host + "]");
        }
        Assert.isTrue(port > 0 && port < 65535, () -> new IllegalArgumentException("invalid port [" + port + "]"));
    }

    private HttpUriRequest loginRequest(String userId) throws URISyntaxException {
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost(host)
                .setPort(port)
                .setPath("login/" + userId)
                .build();
        return new HttpGet(uri);
    }

    private HttpUriRequest submitRequest(String token, String applicationId, long score) throws URISyntaxException {
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost(host)
                .setPort(port)
                .setPath("applications/" + applicationId + "/scores")
                .build();
        HttpPost post = new HttpPost(uri);
        post.addHeader("X-CLIENT-TOKEN", token);
        try {
            post.setEntity(new ByteArrayEntity(MAPPER.writeValueAsBytes(new SubmitScoreRequestBody(score))));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
        return post;
    }

    private HttpUriRequest getTopScoreRequest(String applicationId, long offset, long size) throws URISyntaxException {
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost(host)
                .setPort(port)
                .setPath("applications/" + applicationId + "/scores")
                .setParameter("offset", String.valueOf(offset))
                .setParameter("size", String.valueOf(size))
                .build();
        return new HttpGet(uri);
    }

    private HttpUriRequest searchScoreRequest(String userId, String applicationId, int top, int bottom) throws URISyntaxException {
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost(host)
                .setPort(port)
                .setPath("applications/" + applicationId + "/scores/search")
                .setParameter("userId", userId)
                .setParameter("top", String.valueOf(top))
                .setParameter("bottom", String.valueOf(bottom))
                .build();
        return new HttpGet(uri);
    }
}
