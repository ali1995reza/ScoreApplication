package gram.gs.repository.impl.memory;

import gram.gs.model.RankedScore;
import gram.gs.repository.impl.ValidatedScoreRepository;
import gram.gs.repository.impl.memory.dao.ScoreDAO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static gram.gs.repository.impl.util.BinarySearchUtil.*;

public class InMemoryScoreRepository extends ValidatedScoreRepository {

    private final static Comparator<ScoreDAO> DESC = (s1, s2) -> s1.compareTo(s2) * -1;

    private final ConcurrentHashMap<String, List<ScoreDAO>> scoresIndexByGameId;
    private final ConcurrentHashMap<String, ScoreDAO> scoresIndexByUserIdAndApplicationId;


    public InMemoryScoreRepository() {
        scoresIndexByGameId = new ConcurrentHashMap<>();
        scoresIndexByUserIdAndApplicationId = new ConcurrentHashMap<>();
    }

    @Override
    protected RankedScore doSave(String userId, String applicationId, long score) {
        List<ScoreDAO> scores = scoresIndexByGameId.computeIfAbsent(applicationId, (key) -> new ArrayList<>());
        ScoreDAO userScore = scoresIndexByUserIdAndApplicationId.computeIfAbsent(userIdApplicationIdKey(userId, applicationId), (key) -> new ScoreDAO(userId, applicationId));
        if (userScore.getScore() != null && userScore.getScore() >= score) {
            return doGet(userId, applicationId);
        }
        synchronized (scores) {
            binaryRemove(scores, userScore, DESC);
            userScore.setScore(score);
            final int insertIndex = binaryAddOrSet(scores, userScore, DESC);
            return map(userScore, insertIndex + 1);
        }
    }

    @Override
    protected List<RankedScore> doGet(String applicationId, long offset, long size) {
        List<ScoreDAO> scores = scoresIndexByGameId.computeIfAbsent(applicationId, (key) -> new ArrayList<>());
        synchronized (scores) {
            return scores.stream().skip(offset)
                    .limit(size)
                    .map(RankedScoreMapper.withOffset(offset))
                    .collect(Collectors.toList());
        }
    }

    @Override
    protected List<RankedScore> doGet(String userId, String applicationId, int top, int bottom) {
        ScoreDAO userScore = scoresIndexByUserIdAndApplicationId.get(userIdApplicationIdKey(userId, applicationId));
        if (userScore == null) {
            return null;
        }
        List<ScoreDAO> scores = scoresIndexByGameId.computeIfAbsent(applicationId, (key) -> new ArrayList<>());
        synchronized (scores) {
            final int index = binarySearch(scores, userScore, DESC);
            final long offset = Math.max(0, index - top);
            final long limit = (index - offset) + bottom + 1;
            return scores.stream()
                    .skip(offset)
                    .limit(limit)
                    .map(RankedScoreMapper.withOffset(offset))
                    .collect(Collectors.toList());
        }
    }

    @Override
    protected RankedScore doGet(String userId, String applicationId) {
        ScoreDAO userScore = scoresIndexByUserIdAndApplicationId.get(userIdApplicationIdKey(userId, applicationId));
        if (userScore == null) {
            return null;
        }
        List<ScoreDAO> scores = scoresIndexByGameId.computeIfAbsent(applicationId, (key) -> new ArrayList<>());
        synchronized (scores) {
            final int index = binarySearch(scores, userScore, DESC);
            return map(userScore, index + 1);
        }
    }

    @Override
    public void clear() {
        scoresIndexByUserIdAndApplicationId.clear();
        scoresIndexByGameId.clear();
    }


    private static String userIdApplicationIdKey(String userId, String applicationId) {
        return userId + "#" + applicationId;
    }

    private static RankedScore map(ScoreDAO dao, long rank) {
        return new RankedScore()
                .id(dao.getId())
                .userId(dao.getUserId())
                .applicationId(dao.getApplicationId())
                .score(dao.getScore())
                .rank(rank);
    }
}
