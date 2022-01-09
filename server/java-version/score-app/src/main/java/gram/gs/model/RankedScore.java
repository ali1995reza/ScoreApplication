package gram.gs.model;

public class RankedScore extends AbstractModel<RankedScore> {

    private String userId;
    private String applicationId;
    private Long score;
    private Long rank;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RankedScore userId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public RankedScore applicationId(String applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public RankedScore score(Long score) {
        this.score = score;
        return this;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public RankedScore rank(Long rank) {
        this.rank = rank;
        return this;
    }
}
