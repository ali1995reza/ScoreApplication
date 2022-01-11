package gram.gs.repository.impl.sql;

import gram.gs.model.RankedScore;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class SqlScoreRepository extends SqlScoreRepositoryAbstract {

    private final static Comparator<RankedScore> ASC = new Comparator<RankedScore>() {
        @Override
        public int compare(RankedScore rank1, RankedScore rank2) {
            if (rank1 == rank2) {
                return 0;
            }
            final int result = Long.compare(rank1.getScore(), rank2.getScore());
            if (result != 0) {
                return result;
            }
            return rank1.getUserId().compareTo(rank2.getUserId());
        }
    };
    private final static Comparator<RankedScore> DESC = ASC.reversed();


    private final String tableName = "scores";
    private final String updateSQL = "UPDATE " + tableName + " SET score=? WHERE application_id=? AND user_id=? AND score<?";
    private final String insertSQL = "INSERT INTO " + tableName + " VALUES(?,?,?,?)";
    private final String getListSQL = "SELECT * FROM " + tableName + " WHERE application_id=? ORDER BY score DESC, user_id DESC LIMIT ? OFFSET ?";
    private final String getSQL = "SELECT * FROM " + tableName + " WHERE application_id=? AND user_id=?";
    private final String calculateRankSQL = "SELECT 1+COUNT(*) FROM " + tableName + " WHERE application_id=? AND (score>?  OR (score=? AND user_id>?))";
    private final String getTopAndBottomSQL = "(SELECT p.id, p.user_id, p.application_id, p.score FROM " + tableName + " AS n JOIN " + tableName + " AS p ON (p.application_id=n.application_id AND (p.score>n.score OR (p.score=n.score AND p.user_id>=n.user_id))) WHERE n.application_id=? AND n.user_id=? ORDER BY p.score ASC, p.user_id ASC LIMIT ?) " +
            "UNION " +
            "(SELECT p.id, p.user_id, p.application_id, p.score FROM " + tableName + " AS n JOIN " + tableName + " AS p ON (p.application_id=n.application_id AND (p.score<n.score OR (p.score=n.score AND p.user_id<n.user_id))) WHERE n.application_id=? AND n.user_id=? ORDER BY p.score DESC, p.user_id DESC LIMIT ?)";


    public SqlScoreRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected RankedScore doSave(String userId, String applicationId, long score) {
        if (update(userId, applicationId, score) || insert(userId, applicationId, score)) {
            RankedScore rankedScore = new RankedScore()
                    .userId(userId)
                    .applicationId(applicationId)
                    .score(score);
            return rankedScore
                    .rank(getRank(rankedScore));
        } else {
            return get(userId, applicationId);
        }
    }

    @Override
    protected List<RankedScore> doGet(String applicationId, long offset, long size) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(getListSQL)) {
            statement.setString(1, applicationId);
            statement.setLong(2, size);
            statement.setLong(3, offset);
            ResultSet resultSet = statement.executeQuery();
            List<RankedScore> scores = new ArrayList<>();
            int count = 0;
            while (resultSet.next()) {
                scores.add(readScore(resultSet, offset + (++count)));
            }
            return scores;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected List<RankedScore> doGet(String userId, String applicationId, int top, int bottom) {
        List<RankedScore> scores = getTopAndBottomScoresWithoutRank(userId, applicationId, top, bottom);
        if (scores.isEmpty()) {
            return null;
        }
        long rank = getRank(scores.get(0));
        for (RankedScore score : scores) {
            score.rank(rank++);
        }
        return scores;
    }

    @Override
    protected RankedScore doGet(String userId, String applicationId) {
        RankedScore score = getScoreWithoutRank(userId, applicationId);
        return score.rank(getRank(score));
    }

    @Override
    protected void init() {
        execute("CREATE TABLE IF NOT EXISTS " + tableName + " (id VARCHAR(50) PRIMARY KEY, user_id VARCHAR(50), application_id VARCHAR(50), score BIGINT)");
        execute("CREATE INDEX app_score_user ON " + tableName + " (application_id, score DESC, user_id DESC)");
        execute("CREATE UNIQUE INDEX unique_app_user ON " + tableName + " (application_id, user_id)");
    }

    @Override
    public void clear() {
        truncate(tableName);
    }

    private boolean update(String userId, String applicationId, long score) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setLong(1, score);
            statement.setString(2, applicationId);
            statement.setString(3, userId);
            statement.setLong(4, score);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean insert(String userId, String applicationId, long score) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, userId);
            statement.setString(3, applicationId);
            statement.setLong(4, score);
            try {
                return statement.executeUpdate() > 0;
            } catch (SQLIntegrityConstraintViolationException e) {
                return false;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private RankedScore getScoreWithoutRank(String userId, String applicationId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(getSQL)) {
            statement.setString(1, applicationId);
            statement.setString(2, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return readScoreWithoutRank(resultSet);
            }
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private Long getRank(RankedScore score) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(calculateRankSQL)) {
            statement.setString(1, score.getApplicationId());
            statement.setLong(2, score.getScore());
            statement.setLong(3, score.getScore());
            statement.setString(4, score.getUserId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getLong(1);
            }
            return -1l;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

    }

    private List<RankedScore> getTopAndBottomScoresWithoutRank(String userId, String applicationId, int top, int bottom) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(getTopAndBottomSQL)) {
            statement.setString(1, applicationId);
            statement.setString(2, userId);
            statement.setInt(3, top + 1);
            statement.setString(4, applicationId);
            statement.setString(5, userId);
            statement.setInt(6, bottom);
            ResultSet resultSet = statement.executeQuery();
            List<RankedScore> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(readScoreWithoutRank(resultSet));
            }
            list.sort(DESC);
            return list;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private static RankedScore readScoreWithoutRank(ResultSet resultSet) throws SQLException {
        return new RankedScore()
                .id(resultSet.getString(1))
                .userId(resultSet.getString(2))
                .applicationId(resultSet.getString(3))
                .score(resultSet.getLong(4));
    }

    private static RankedScore readScore(ResultSet resultSet, long rank) throws SQLException {
        return readScoreWithoutRank(resultSet)
                .rank(rank);
    }

}
