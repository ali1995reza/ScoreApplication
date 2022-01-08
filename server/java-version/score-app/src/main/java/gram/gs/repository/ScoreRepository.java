package gram.gs.repository;

import gram.gs.model.RankedScore;

import java.util.List;

public interface ScoreRepository {

    RankedScore save(String userId, String applicationId, int score);

    List<RankedScore> get(String applicationId, long offset, long size);

    List<RankedScore> get(String userId, String applicationId, int top, int bottom);

    RankedScore get(String userId, String applicationId);

    void clear();

}
