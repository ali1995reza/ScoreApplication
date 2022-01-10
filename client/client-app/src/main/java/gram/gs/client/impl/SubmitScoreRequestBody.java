package gram.gs.client.impl;

public class SubmitScoreRequestBody {

    private long score;

    public SubmitScoreRequestBody(long score) {
        this.score = score;
    }

    public SubmitScoreRequestBody() {

    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
