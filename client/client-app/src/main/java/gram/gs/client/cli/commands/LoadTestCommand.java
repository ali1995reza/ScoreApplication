package gram.gs.client.cli.commands;

import gram.gs.client.command.annoations.Command;
import gram.gs.client.command.annoations.CommandOption;

@Command("load")
public class LoadTestCommand {

    @CommandOption(longName = "nusers", shortName = "nu")
    private int numberOfUsers;
    @CommandOption(longName = "napps", shortName = "na")
    private int numberOfApplications;
    @CommandOption(longName = "nthreads", shortName = "nt")
    private int numberOfThreads;
    @CommandOption(longName = "nrequests", shortName = "nr")
    private int requestPerThread;
    @CommandOption(longName = "uperiod", shortName = "up", required = false)
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
