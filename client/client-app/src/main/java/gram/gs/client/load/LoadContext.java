package gram.gs.client.load;

import gram.gs.client.abs.ScoreApplicationClient;
import gram.gs.client.abs.dto.RankedScore;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

final class LoadContext {

    private final ConcurrentHashMap<Integer, String> tokens;
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
            if(scoresHolder.size() == 0) {
                return null;
            }
            return scoresHolder.get(random.nextInt(scoresHolder.size()));
        }
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
        synchronized (scoresHolder) {
            if (scoresHolder.size() > 10000) {
                scoresHolder.removeFirst();
            }
            scoresHolder.addLast(new SearchDetails(score.getUserId(), score.getApplicationId()));
        }
    }
}
