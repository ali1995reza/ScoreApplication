package gram.gs.client.load;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

public final class LoadTestMetrics {

    static LoadTestMetrics from(MetricRegistry registry) {
        return new LoadTestMetrics(
                registry.timer("submit-timer"),
                registry.timer("get-list-timer"),
                registry.timer("search-timer"),
                registry.counter("exception-requests-counter"),
                registry.counter("success-requests-counter")
        );
    }

    private final Timer submitTimer;
    private final Timer getListTimer;
    private final Timer searchTimer;
    private final Counter exceptionRequestCounter;
    private final Counter successRequestCounter;

    LoadTestMetrics(Timer submitTimer, Timer getListTimer, Timer searchTimer, Counter exceptionRequestCounter, Counter successRequestCounter) {
        this.submitTimer = submitTimer;
        this.getListTimer = getListTimer;
        this.searchTimer = searchTimer;
        this.exceptionRequestCounter = exceptionRequestCounter;
        this.successRequestCounter = successRequestCounter;
    }

    public Counter getExceptionRequestCounter() {
        return exceptionRequestCounter;
    }

    public Counter getSuccessRequestCounter() {
        return successRequestCounter;
    }

    public Timer getGetListTimer() {
        return getListTimer;
    }

    public Timer getSearchTimer() {
        return searchTimer;
    }

    public Timer getSubmitTimer() {
        return submitTimer;
    }
}

