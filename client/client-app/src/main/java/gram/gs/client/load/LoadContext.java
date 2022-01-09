package gram.gs.client.load;

import gram.gs.client.abs.ScoreApplicationClient;
import gram.gs.client.abs.dto.RankedScore;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class LoadContext {

    private final ScoreApplicationClient client;
    private final ConcurrentHashMap<Integer, String> tokens;
    private final ConcurrentHashMap<Integer, RankedScore> lsatSubmittedScore;
    private final int numberOfUsers;
    private final int numberOfApps;
    private final Random random = new Random();

    public LoadContext(ScoreApplicationClient client, int numberOfUsers, int numberOfApps) {
        this.client = client;
        this.tokens = new ConcurrentHashMap<>();
        this.lsatSubmittedScore = new ConcurrentHashMap<>();
        this.numberOfUsers = numberOfUsers;
        this.numberOfApps = numberOfApps;

    }

    public String getRandomToken() {
        return getTokenFor(random.nextInt(numberOfUsers));
    }

    public String getRandomUserId() {
        return "USER-" + (random.nextInt(numberOfUsers) + 1);
    }

    public String getRandomApplicationId() {
        return "APP-" + (random.nextInt(numberOfApps) + 1);
    }

    private String getTokenFor(int userIndex) {
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
