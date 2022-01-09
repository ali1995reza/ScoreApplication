package gram.gs.client.abs;

import gram.gs.client.abs.dto.ClientToken;
import gram.gs.client.abs.dto.RankedScore;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ScoreApplicationClient {

    CompletableFuture<ClientToken> login(String userId);

    CompletableFuture<RankedScore> submitScore(String token, String applicationId, long score);

    CompletableFuture<List<RankedScore>> getTopScoreList(String applicationId, long offset, long size);

    CompletableFuture<List<RankedScore>> searchScoreList(String userId, String applicationId, int top, int bottom);
}
