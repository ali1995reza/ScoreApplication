package gram.gs.client.load;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import gram.gs.client.abs.ScoreApplicationClient;
import gram.gs.client.impl.ScoreServerError;

import java.util.Random;
import java.util.concurrent.ExecutionException;

final class LoadClient implements Runnable {

    public static Builder newBuilder(ScoreApplicationClient client) {
        return new Builder(client);
    }

    public final static class Builder {
        private final ScoreApplicationClient client;
        private Timer submitTimer;
        private Timer getListTimer;
        private Timer searchTimer;
        private int totalRequests;
        private Counter exceptionRequestCounter;
        private Counter successRequestCounter;
        private LoadContext context;
        private Runnable whenDone;

        private Builder(ScoreApplicationClient client) {
            this.client = client;
        }

        public ScoreApplicationClient getClient() {
            return client;
        }

        public Timer getSubmitTimer() {
            return submitTimer;
        }

        public void setSubmitTimer(Timer submitTimer) {
            this.submitTimer = submitTimer;
        }

        public Builder submitTimer(Timer submitTimer) {
            this.submitTimer = submitTimer;
            return this;
        }

        public Timer getGetListTimer() {
            return getListTimer;
        }

        public void setGetListTimer(Timer getListTimer) {
            this.getListTimer = getListTimer;
        }

        public Builder getListTimer(Timer getListTimer) {
            this.getListTimer = getListTimer;
            return this;
        }

        public Timer getSearchTimer() {
            return searchTimer;
        }

        public void setSearchTimer(Timer searchTimer) {
            this.searchTimer = searchTimer;
        }

        public Builder searchTimer(Timer searchTimer) {
            this.searchTimer = searchTimer;
            return this;
        }

        public int getTotalRequests() {
            return totalRequests;
        }

        public void setTotalRequests(int totalRequests) {
            this.totalRequests = totalRequests;
        }

        public Builder totalRequests(int totalRequests) {
            this.totalRequests = totalRequests;
            return this;
        }

        public Counter getExceptionRequestCounter() {
            return exceptionRequestCounter;
        }

        public void setExceptionRequestCounter(Counter exceptionRequestCounter) {
            this.exceptionRequestCounter = exceptionRequestCounter;
        }

        public Builder exceptionRequestCounter(Counter exceptionRequestCounter) {
            this.exceptionRequestCounter = exceptionRequestCounter;
            return this;
        }

        public Counter getSuccessRequestCounter() {
            return successRequestCounter;
        }

        public void setSuccessRequestCounter(Counter successRequestCounter) {
            this.successRequestCounter = successRequestCounter;
        }

        public Runnable getWhenDone() {
            return whenDone;
        }

        public void setWhenDone(Runnable whenDone) {
            this.whenDone = whenDone;
        }

        public Builder whenDone(Runnable whenDone) {
            this.whenDone = whenDone;
            return this;
        }

        public Builder successRequestCounter(Counter successRequestCounter) {
            this.successRequestCounter = successRequestCounter;
            return this;
        }

        public LoadContext getContext() {
            return context;
        }

        public void setContext(LoadContext context) {
            this.context = context;
        }

        public Builder context(LoadContext context) {
            this.context = context;
            return this;
        }

        public LoadClient build() {
            return new LoadClient(client,
                    submitTimer,
                    getListTimer,
                    searchTimer,
                    totalRequests,
                    exceptionRequestCounter,
                    successRequestCounter,
                    context,
                    whenDone);
        }
    }


    private final static int CALL_SUBMIT = 0;
    private final static int CALL_GET_LIST = 1;
    private final static int CALL_SEARCH = 2;

    private final ScoreApplicationClient client;
    private final Timer submitTimer;
    private final Timer getListTimer;
    private final Timer searchTimer;
    private int totalRequests;
    private final Counter exceptionRequestCounter;
    private final Counter successRequestCounter;
    private final LoadContext context;
    private final Runnable whenDoneListener;
    private Random random = new Random();

    private LoadClient(ScoreApplicationClient client, Timer submitTimer, Timer getListTimer, Timer searchTimer, int totalRequests, Counter exceptionRequestCounter, Counter successRequestCounter, LoadContext context, Runnable whenDoneListener) {
        this.client = client;
        this.submitTimer = submitTimer;
        this.getListTimer = getListTimer;
        this.searchTimer = searchTimer;
        this.totalRequests = totalRequests;
        this.exceptionRequestCounter = exceptionRequestCounter;
        this.successRequestCounter = successRequestCounter;
        this.context = context;
        this.whenDoneListener = whenDoneListener == null ? () -> {
        } : whenDoneListener;
    }

    @Override
    public void run() {
        this.mainLoop();
    }

    private int whatToDo() {
        return random.nextInt(3);
    }

    public void mainLoop() {
        long lastScore = 0;
        while (totalRequests-- > 0) {
            switch (whatToDo()) {
                case CALL_SUBMIT:
                    long score = lastScore + random.nextInt(4) + 1;
                    try (Timer.Context time = submitTimer.time()) {
                        lastScore = client.submitScore(context.getRandomToken(client), context.getRandomApplicationId(), score).get().getScore();
                        successRequestCounter.inc();
                    } catch (ExecutionException e) {
                        if (e.getCause() instanceof ScoreServerError) {
                            successRequestCounter.inc();
                        } else {
                            exceptionRequestCounter.inc();
                        }
                    } catch (Exception e) {
                        exceptionRequestCounter.inc();
                        e.printStackTrace();
                    }
                    break;
                case CALL_GET_LIST:
                    try (Timer.Context time = getListTimer.time()) {
                        client.getTopScoreList(context.getRandomApplicationId(), random.nextInt(1000), 1 + random.nextInt(1000)).get();
                        successRequestCounter.inc();
                    } catch (ExecutionException e) {
                        if (e.getCause() instanceof ScoreServerError) {
                            successRequestCounter.inc();
                        } else {
                            exceptionRequestCounter.inc();
                        }
                    } catch (Exception e) {
                        exceptionRequestCounter.inc();
                        e.printStackTrace();
                    }
                    break;
                case CALL_SEARCH:
                    try (Timer.Context time = searchTimer.time()) {
                        client.searchScoreList(context.getRandomUserId(), context.getRandomApplicationId(), random.nextInt(1000), random.nextInt(1000)).get();
                        successRequestCounter.inc();
                    } catch (ExecutionException e) {
                        if (e.getCause() instanceof ScoreServerError) {
                            successRequestCounter.inc();
                        } else {
                            exceptionRequestCounter.inc();
                        }
                    } catch (Exception e) {
                        exceptionRequestCounter.inc();
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
        whenDoneListener.run();
    }
}
