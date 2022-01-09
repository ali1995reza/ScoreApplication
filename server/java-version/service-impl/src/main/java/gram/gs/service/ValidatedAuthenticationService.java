package gram.gs.service;

import gram.gs.assertion.Assert;

import java.util.concurrent.TimeUnit;

public abstract class ValidatedAuthenticationService implements AuthenticationService {


    @Override
    public String createToken(String userId, long duration, TimeUnit timeUnit) {
        Assert.isNotNull(userId, () -> new NullPointerException("user id is null"));
        Assert.isPositive(duration, () -> new IllegalArgumentException("duration must be positive"));
        Assert.isNotNull(timeUnit, () -> new NullPointerException("time unit is null"));
        return doCreateToken(userId, duration, timeUnit);
    }

    protected abstract String doCreateToken(String userId, long duration, TimeUnit timeUnit);

}
