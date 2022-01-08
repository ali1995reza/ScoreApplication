package gram.gs.repository.impl.memory;

import gram.gs.model.RankedScore;
import gram.gs.repository.ScoreRepository;
import gram.gs.repository.impl.memory.dao.ScoreDAO;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryScoreRepository implements ScoreRepository {

    private final static Comparator<ScoreDAO> DESC = (s1, s2) -> s1.compareTo(s2) * -1;

    private final ConcurrentHashMap<String, List<ScoreDAO>> scoresIndexByGameId;
    private final ConcurrentHashMap<String, ScoreDAO> scoresIndexByUserIdAndGameId;


    public InMemoryScoreRepository() {
        scoresIndexByGameId = new ConcurrentHashMap<>();
        scoresIndexByUserIdAndGameId = new ConcurrentHashMap<>();
    }

    @Override
    public RankedScore save(String s, String s1, int i) {
        return null;
    }

    @Override
    public List<RankedScore> get(String s, long l, long l1) {
        return null;
    }

    @Override
    public List<RankedScore> get(String s, String s1, int i, int i1) {
        return null;
    }

    @Override
    public RankedScore get(String s, String s1) {
        return null;
    }

    @Override
    public void clear() {

    }
}
