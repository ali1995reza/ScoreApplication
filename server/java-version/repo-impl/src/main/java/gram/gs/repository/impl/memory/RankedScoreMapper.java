package gram.gs.repository.impl.memory;

import gram.gs.model.RankedScore;
import gram.gs.repository.impl.memory.dao.ScoreDAO;

import java.util.function.Function;

final class RankedScoreMapper implements Function<ScoreDAO, RankedScore> {

    public static RankedScoreMapper withOffset(long offset) {
        return new RankedScoreMapper(offset);
    }

    private long offset;

    private RankedScoreMapper(long offset) {
        this.offset = offset;
    }

    @Override
    public RankedScore apply(ScoreDAO dao) {
        return new RankedScore()
                .id(dao.getId())
                .userId(dao.getUserId())
                .applicationId(dao.getApplicationId())
                .score(dao.getScore())
                .rank(++offset);
    }
}
