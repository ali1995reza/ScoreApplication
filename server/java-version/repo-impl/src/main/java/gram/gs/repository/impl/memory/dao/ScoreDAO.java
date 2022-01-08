package gram.gs.repository.impl.memory.dao;

import java.util.Objects;

public class ScoreDAO implements Comparable<ScoreDAO> {

    private String userId;
    private String applicationId;
    private Long score;

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
        if(this == other) {
            return 0;
        }
        int result = Long.compare(this.score, other.score);
        if(result != 0) {
            return result;
        }
        return this.userId.compareTo(other.userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoreDAO scoreDAO = (ScoreDAO) o;
        return Objects.equals(userId, scoreDAO.userId) && Objects.equals(applicationId, scoreDAO.applicationId) && Objects.equals(score, scoreDAO.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, applicationId, score);
    }
}
