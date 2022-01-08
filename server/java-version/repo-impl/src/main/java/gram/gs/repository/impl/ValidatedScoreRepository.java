package gram.gs.repository.impl;

import gram.gs.assertion.Assert;
import gram.gs.model.RankedScore;
import gram.gs.repository.ScoreRepository;

import java.util.List;

public abstract class ValidatedScoreRepository implements ScoreRepository {

    @Override
    public RankedScore save(String userId, String applicationId, long score) {
        Assert.isNotNull(userId, () -> new NullPointerException("user id is null"));
        Assert.isNotNull(applicationId, () -> new NullPointerException("application id is null"));
        Assert.isNotNegative(score, () -> new IllegalArgumentException("score can not be negative"));
        return doSave(userId, applicationId, score);
    }

    @Override
    public List<RankedScore> get(String applicationId, long offset, long size) {
        Assert.isNotNull(applicationId, () -> new NullPointerException("application id is null"));
        Assert.isNotNegative(offset, () -> new IllegalArgumentException("offset can not be negative"));
        Assert.isNotNegative(size, () -> new IllegalArgumentException("size can not be negative"));
        return doGet(applicationId, offset, size);
    }

    @Override
    public List<RankedScore> get(String userId, String applicationId, int top, int bottom) {
        Assert.isNotNull(userId, () -> new NullPointerException("user id is null"));
        Assert.isNotNull(applicationId, () -> new NullPointerException("application id is null"));
        Assert.isNotNegative(top, () -> new IllegalArgumentException("top can not be negative"));
        Assert.isNotNegative(bottom, () -> new IllegalArgumentException("bottom can not be negative"));
        return doGet(userId, applicationId, top, bottom);
    }

    @Override
    public RankedScore get(String userId, String applicationId) {
        Assert.isNotNull(userId, () -> new NullPointerException("user id is null"));
        Assert.isNotNull(applicationId, () -> new NullPointerException("application id is null"));
        return doGet(userId, applicationId);
    }

    protected abstract RankedScore doSave(String userId, String applicationId, long score);

    protected abstract List<RankedScore> doGet(String applicationId, long offset, long size);

    protected abstract List<RankedScore> doGet(String userId, String applicationId, int top, int bottom);

    protected abstract RankedScore doGet(String userId, String applicationId);
}
