package gram.gs.client.load;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import gram.gs.client.abs.ScoreApplicationClient;
import gram.gs.client.impl.HttpScoreApplicationClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadTester {
    public static void main(String[] args) throws Exception {
        MetricRegistry metrics = new MetricRegistry();
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(5, TimeUnit.SECONDS);
        AtomicInteger integer = new AtomicInteger(1000000);
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(LoadClient
                    .newBuilder(newClient())
                    .submitTimer(metrics.timer("submit"))
                    .getListTimer(metrics.timer("get-list"))
                    .searchTimer(metrics.timer("search"))
                    .exceptionRequestCounter(metrics.counter("exceptions"))
                    .successRequestCounter(metrics.counter("success"))
                    .totalRequests(100000)
                    .context(new LoadContext(newClient(), 10000, 100))
                    .whenDone(latch::countDown)
                    .build())
                    .start();
        }
        latch.await();
        System.exit(10);
    }


    public static ScoreApplicationClient newClient() {
        CloseableHttpAsyncClient client = HttpAsyncClients.custom()
                .setDefaultIOReactorConfig(
                        IOReactorConfig.custom()
                                .setIoThreadCount(1)
                                .build()
                ).build();
        client.start();
        return new HttpScoreApplicationClient(client);
    }

}
