package gram.gs.server.abs;

import gram.gs.ScoreApplication;
import gram.gs.assertion.Assert;

public abstract class ScoreAppHttpServer {

    private ScoreApplication application;
    private String host;
    private int port;
    private int numberOfHandlerThreads;
    private boolean isRunning = false;

    public synchronized ScoreApplication getApplication() {
        return application;
    }

    public synchronized void setApplication(ScoreApplication application) {
        this.application = application;
    }

    public synchronized ScoreAppHttpServer application(ScoreApplication application) {
        Assert.isNotNull(application, () -> new NumberFormatException("provided application is null"));
        this.application = application;
        return this;
    }

    public synchronized String getHost() {
        return host;
    }

    public synchronized ScoreAppHttpServer setHost(String host) {
        Assert.isNotNull(host, () -> new NullPointerException("host is null"));
        this.host = host;
        return this;
    }

    public synchronized ScoreAppHttpServer host(String host) {
        Assert.isNotNull(host, () -> new NullPointerException("host is null"));
        this.host = host;
        return this;
    }

    public synchronized int getPort() {
        return port;
    }

    public synchronized void setPort(int port) {
        Assert.isTrue(port > 1024 && port < 65535, () -> new IllegalArgumentException("port range is (1024,65535)"));
        this.port = port;
    }

    public synchronized ScoreAppHttpServer port(int port) {
        Assert.isTrue(port > 1024 && port < 65535, () -> new IllegalArgumentException("port range is (1024,65535)"));
        this.port = port;
        return this;
    }

    public synchronized int getNumberOfHandlerThreads() {
        return numberOfHandlerThreads;
    }

    public synchronized void setNumberOfHandlerThreads(int numberOfHandlerThreads) {
        Assert.isPositive(numberOfHandlerThreads, () -> new IllegalArgumentException("number of handle threads must be positive"));
        this.numberOfHandlerThreads = numberOfHandlerThreads;
    }

    public synchronized ScoreAppHttpServer numberOfHandlerThreads(int numberOfHandlerThreads) {
        Assert.isPositive(numberOfHandlerThreads, () -> new IllegalArgumentException("number of handle threads must be positive"));
        this.numberOfHandlerThreads = numberOfHandlerThreads;
        return this;
    }

    public synchronized ScoreAppHttpServer start() {
        Assert.isFalse(isRunning, () -> new IllegalStateException("server already running"));
        Assert.isNotNull(application, () -> new NumberFormatException("provided application is null"));
        Assert.isNotNull(host, () -> new NullPointerException("host is null"));
        Assert.isTrue(port > 1024 && port < 65535, () -> new IllegalArgumentException("port range is (1024,65535)"));
        Assert.isPositive(numberOfHandlerThreads, () -> new IllegalArgumentException("number of handle threads must be positive"));
        doStart(application, host, port, numberOfHandlerThreads);
        isRunning = true;
        return this;
    }

    protected abstract void doStart(ScoreApplication application, String host, int port, int numberOfHandlerThreads);

}
