package gram.gs.client.load;

import gram.gs.client.abs.ScoreApplicationClient;
import gram.gs.client.abs.dto.ClientToken;
import gram.gs.client.abs.dto.RankedScore;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

final class LoadContext {

    private final ConcurrentHashMap<Integer, TokenHolder> tokens;
    private final int numberOfUsers;
    private final int numberOfApps;
    private final Random random = new Random();
    private final LinkedList<SearchDetails> scoresHolder;

    public LoadContext(int numberOfUsers, int numberOfApps) {
        this.tokens = new ConcurrentHashMap<>();
        this.numberOfUsers = numberOfUsers;
        this.numberOfApps = numberOfApps;
        this.scoresHolder = new LinkedList<>();

    }

    public String getRandomToken(ScoreApplicationClient client) {
        return getTokenFor(random.nextInt(numberOfUsers), client);
    }

    public String getRandomUserId() {
        return "USER-" + (random.nextInt(numberOfUsers) + 1);
    }

    public SearchDetails getRandomSearchDetails() {
        synchronized (scoresHolder) {
            if (scoresHolder.size() == 0) {
                return null;
            }
            return scoresHolder.get(random.nextInt(scoresHolder.size()));
        }
    }

    public String getRandomApplicationId() {
        return "APP-" + (random.nextInt(numberOfApps) + 1);
    }

    private String getTokenFor(int userIndex, ScoreApplicationClient client) {
        return tokens.compute(userIndex, (index, last) -> {
            if (System.currentTimeMillis() - last.getCreatedTime() < TimeUnit.MINUTES.toMillis(9)) {
                return last;
            }
            try {
                return TokenHolder.from(client.login("USER-" + index).get());
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }).getToken();
    }

    public void submit(RankedScore score) {
        synchronized (scoresHolder) {
            if (scoresHolder.size() > 10000) {
                scoresHolder.removeFirst();
            }
            scoresHolder.addLast(new SearchDetails(score.getUserId(), score.getApplicationId()));
        }
    }

    private final static class TokenHolder {

        private final String token;
        private final long createdTime;

        private TokenHolder(String token, long createdTime) {
            this.token = token;
            this.createdTime = createdTime;
        }

        private static TokenHolder from(ClientToken token) {
            return new TokenHolder(token.getToken(), System.currentTimeMillis());
        }

        public long getCreatedTime() {
            return createdTime;
        }

        public String getToken() {
            return token;
        }
    }
}
