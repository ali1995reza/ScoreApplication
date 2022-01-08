package gram.gs.repository.impl.memory.dao;

public class ScoreDAO {

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
}
