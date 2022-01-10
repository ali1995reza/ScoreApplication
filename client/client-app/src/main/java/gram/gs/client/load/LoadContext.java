package gram.gs.client.load;

import gram.gs.client.abs.ScoreApplicationClient;
import gram.gs.client.abs.dto.RankedScore;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

final class LoadContext {

    private final ConcurrentHashMap<Integer, String> tokens;
    private final ConcurrentHashMap<Integer, RankedScore> lsatSubmittedScore;
    private final int numberOfUsers;
    private final int numberOfApps;
    private final Random random = new Random();

    public LoadContext(int numberOfUsers, int numberOfApps) {
        this.tokens = new ConcurrentHashMap<>();
        this.lsatSubmittedScore = new ConcurrentHashMap<>();
        this.numberOfUsers = numberOfUsers;
        this.numberOfApps = numberOfApps;

    }

    public String getRandomToken(ScoreApplicationClient client) {
        return getTokenFor(random.nextInt(numberOfUsers), client);
    }

    public String getRandomUserId() {
        return "USER-" + (random.nextInt(numberOfUsers) + 1);
    }

    public String getRandomApplicationId() {
        return "APP-" + (random.nextInt(numberOfApps) + 1);
    }

    private String getTokenFor(int userIndex, ScoreApplicationClient client) {
        return tokens.computeIfAbsent(userIndex, (index) -> {
            try {
                return client.login("USER-" + index).get().getToken();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }

    public void submit(RankedScore score) {
        //todo something here
    }
}
