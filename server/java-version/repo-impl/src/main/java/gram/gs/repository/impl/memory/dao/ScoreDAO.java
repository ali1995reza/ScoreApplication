package gram.gs.repository.impl.memory.dao;

import java.util.Objects;
import java.util.UUID;

public class ScoreDAO implements Comparable<ScoreDAO> {

    private final String id;
    private String userId;
    private String applicationId;
    private Long score;

    public ScoreDAO(String userId, String applicationId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.applicationId = applicationId;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ScoreDAO userId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public ScoreDAO applicationId(String applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public ScoreDAO score(Long score) {
        this.score = score;
        return this;
    }

    @Override
    public int compareTo(ScoreDAO other) {
        if (this == other) {
            return 0;
        }
        final int result = Long.compare(this.score, other.score);
        if (result != 0) {
            return result;
        }
        return this.userId.compareTo(other.userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoreDAO scoreDAO = (ScoreDAO) o;
        return Objects.equals(id, scoreDAO.id) && Objects.equals(userId, scoreDAO.userId) && Objects.equals(applicationId, scoreDAO.applicationId) && Objects.equals(score, scoreDAO.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, applicationId, score);
    }
}
