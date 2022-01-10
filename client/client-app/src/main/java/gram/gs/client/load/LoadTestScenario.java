package gram.gs.client.load;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import gram.gs.client.abs.ScoreApplicationClient;
import gram.gs.client.impl.HttpScoreApplicationClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class LoadTestScenario {

    public static Builder newBuilder() {
        return new Builder();
    }

    public final static class Builder {

        private int numberOfUsers;
        private int numberOfApplications;
        private int numberOfThreads;
        private int requestPerThread;

        private Builder() {

        }

        public int getNumberOfUsers() {
            return numberOfUsers;
        }

        public void setNumberOfUsers(int numberOfUsers) {
            this.numberOfUsers = numberOfUsers;
        }

        public Builder numberOfUsers(int numberOfUsers) {
            this.numberOfUsers = numberOfUsers;
            return this;
        }

        public int getNumberOfApplications() {
            return numberOfApplications;
        }

        public void setNumberOfApplications(int numberOfApplications) {
            this.numberOfApplications = numberOfApplications;
        }

        public Builder numberOfApplications(int numberOfApplications) {
            this.numberOfApplications = numberOfApplications;
            return this;
        }

        public int getNumberOfThreads() {
            return numberOfThreads;
        }

        public void setNumberOfThreads(int numberOfThreads) {
            this.numberOfThreads = numberOfThreads;
        }

        public Builder numberOfThreads(int numberOfThreads) {
            this.numberOfThreads = numberOfThreads;
            return this;
        }

        public int getRequestPerThread() {
            return requestPerThread;
        }

        public void setRequestPerThread(int requestPerThread) {
            this.requestPerThread = requestPerThread;
        }

        public Builder requestPerThread(int requestPerThread) {
            this.requestPerThread = requestPerThread;
            return this;
        }

        public LoadTestScenario build() {
            return new LoadTestScenario(numberOfUsers, numberOfApplications, numberOfThreads, requestPerThread);
        }
    }

    private final int numberOfUsers;
    private final int numberOfApplications;
    private final int numberOfThreads;
    private final int requestPerThread;
    private final MetricRegistry metrics;

    public LoadTestScenario(int numberOfUsers, int numberOfApplications, int numberOfThreads, int requestPerThread) {
        this.numberOfUsers = numberOfUsers;
        this.numberOfApplications = numberOfApplications;
        this.numberOfThreads = numberOfThreads;
        this.requestPerThread = requestPerThread;
        this.metrics = new MetricRegistry();
    }

    public void run(long updatePeriod, TimeUnit unit, Consumer<LoadTestMetrics> listener) {
        final CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom()
                .setDefaultIOReactorConfig(
                        IOReactorConfig.custom()
                                .setIoThreadCount(numberOfThreads)
                                .build()
                ).build();
        httpClient.start();
        ScoreApplicationClient client = new HttpScoreApplicationClient(httpClient);
        final ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        final LoadTestMetrics testMetrics = LoadTestMetrics.from(metrics);
        final LoadContext context = new LoadContext(numberOfUsers, numberOfApplications);
        final CountDownLatch remainingThreads = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            executor.execute(
                    LoadClient
                            .newBuilder(client)
                            .submitTimer(testMetrics.getSubmitTimer())
                            .getListTimer(testMetrics.getGetListTimer())
                            .searchTimer(testMetrics.getSearchTimer())
                            .exceptionRequestCounter(testMetrics.getExceptionRequestCounter())
                            .successRequestCounter(testMetrics.getSuccessRequestCounter())
                            .totalRequests(requestPerThread)
                            .context(context)
                            .whenDone(remainingThreads::countDown)
                            .build()
            );
            while (remainingThreads.getCount() > 0) {

                try {
                    wait(updatePeriod, unit);
                    listener.accept(testMetrics);
                } catch (Exception e) {
                    try {
                        httpClient.close();
                    } catch (IOException ex) {
                    }
                    executor.shutdownNow();
                    throw new IllegalStateException(e);
                }

            }

            try {
                httpClient.close();
            } catch (IOException ex) {
            }
            executor.shutdownNow();
        }
    }

    private static void wait(long time, TimeUnit unit) throws InterruptedException {
        Thread.sleep(unit.toMillis(time));
    }
}
