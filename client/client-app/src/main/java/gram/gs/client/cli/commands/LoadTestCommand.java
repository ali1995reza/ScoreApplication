package gram.gs.client.cli.commands;

import gram.gs.client.command.annoations.Command;
import gram.gs.client.command.annoations.CommandOption;

@Command("load")
public class LoadTestCommand {

    @CommandOption(longName = "nusers", shortName = "nu", description = "number of simulated users")
    private int numberOfUsers;
    @CommandOption(longName = "napps", shortName = "na", description = "number of simulated apps")
    private int numberOfApplications;
    @CommandOption(longName = "nthreads", shortName = "nt", description = "number of parallel threads that send requests to server")
    private int numberOfThreads;
    @CommandOption(longName = "nrequests", shortName = "nr", description = "number of request that each thread will send. total-requests = nt*nr")
    private int requestPerThread;
    @CommandOption(longName = "uperiod", shortName = "up", required = false, description = "statistic print-period time in seconds. default = 1")
    private int updatePeriod = 1;

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public int getNumberOfApplications() {
        return numberOfApplications;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public int getRequestPerThread() {
        return requestPerThread;
    }

    public int getUpdatePeriod() {
        return updatePeriod;
    }
}
