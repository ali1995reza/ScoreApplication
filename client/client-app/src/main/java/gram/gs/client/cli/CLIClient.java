package gram.gs.client.cli;

import com.codahale.metrics.Timer;
import dnl.utils.text.table.TextTable;
import gram.gs.client.abs.ScoreApplicationClient;
import gram.gs.client.abs.dto.ClientToken;
import gram.gs.client.abs.dto.RankedScore;
import gram.gs.client.cli.commands.*;
import gram.gs.client.command.parser.CommandParser;
import gram.gs.client.command.parser.builder.CommandParserBuilder;
import gram.gs.client.command.standard.ExitCommand;
import gram.gs.client.command.standard.HelpCommand;
import gram.gs.client.impl.HttpScoreApplicationClient;
import gram.gs.client.load.LoadTestMetrics;
import gram.gs.client.load.LoadTestScenario;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class CLIClient {

    private final static String[] LOAD_TEST_TABLE_HEADERS = new String[]{
            " Method ",
            " Total Calls ",
            " Mean Time [ms]",
            " Maximum Time [ms]",
            " Minimum Time [ms]",
            " Mean Rate [call/sec]",
            " 1 Min Rate [call/sec]",
            " 5 Min Rate [call/sec]"
    };
    private final static DecimalFormat FMT = new DecimalFormat("0.00");

    private final CommandParser commandParser;
    private final ScoreApplicationClient client;
    private ClientToken token;
    private String currentUser;


    public CLIClient(String host, int port) {
        commandParser = CommandParserBuilder
                .builder()
                .add(LoginCommand.class)
                .add(LogoutCommand.class)
                .add(SearchScoreCommand.class)
                .add(SubmitScoreCommand.class)
                .add(GetTopScoreCommand.class)
                .add(LoadTestCommand.class)
                .add(ExitCommand.class)
                .add(HelpCommand.class)
                .build()
                .setCommandHandler(LoginCommand.class, this::handleLogin)
                .setCommandHandler(LogoutCommand.class, this::handleLogout)
                .setCommandHandler(SearchScoreCommand.class, this::handleSearchScore)
                .setCommandHandler(SubmitScoreCommand.class, this::handleSubmitScore)
                .setCommandHandler(GetTopScoreCommand.class, this::handleGetTopScoreList)
                .setCommandHandler(LoadTestCommand.class, this::handleLoadTest);

        client = new HttpScoreApplicationClient(host, port);
    }

    public synchronized int run(Supplier<String> commandSupplier) {
        while (true) {
            printCommandInterceptor();
            String command = commandSupplier.get();
            try {
                Object commandObject = commandParser.parse(command);
                if (commandObject instanceof ExitCommand) {
                    ExitCommand exitCommand = (ExitCommand) commandObject;
                    return exitCommand.getCode();
                }
            } catch (Exception e) {
                System.out.println("ERROR : " + e.getMessage()+"\r\n");
            }
        }
    }

    private void printCommandInterceptor() {
        if (token == null) {
            System.out.print("- >> ");
        } else {
            System.out.print(currentUser + " >> ");
        }
    }

    private void handleLogin(LoginCommand command) throws Exception {
        long start = System.currentTimeMillis();
        this.token = client.login(command.getUserId()).get();
        long end = System.currentTimeMillis();
        this.currentUser = command.getUserId();
        System.out.println("Logged id as : " + currentUser);
        printTime(end - start);
    }

    private void handleLogout(LogoutCommand command) throws Exception {
        long start = System.currentTimeMillis();
        if (token == null) {
            throw new IllegalStateException("you are not logged in !");
        }
        this.token = null;
        this.currentUser = null;
        long end = System.currentTimeMillis();
        System.out.println("Logout !");
        printTime(end - start);
    }

    private void handleGetTopScoreList(GetTopScoreCommand command) throws Exception {
        long start = System.currentTimeMillis();
        List<RankedScore> scores = client.getTopScoreList(
                command.getApplicationId(),
                command.getOffset(),
                command.getSize()
        ).get();
        long end = System.currentTimeMillis();
        printInTable(scores);
        printTime(end - start);
    }

    private void handleSearchScore(SearchScoreCommand command) throws Exception {
        long start = System.currentTimeMillis();
        List<RankedScore> scores = client.searchScoreList(
                command.getUserId(),
                command.getApplicationId(),
                command.getTop(),
                command.getBottom()
        ).get();
        long end = System.currentTimeMillis();
        printInTable(scores);
        printTime(end - start);
    }

    private void handleSubmitScore(SubmitScoreCommand command) throws Exception {
        long start = System.currentTimeMillis();
        if (token == null) {
            throw new IllegalStateException("please login first !");
        }
        RankedScore score = client.submitScore(
                token.getToken(),
                command.getApplicationId(),
                command.getScore()
        ).get();
        long end = System.currentTimeMillis();
        printInTable(List.of(score));
        printTime(end - start);
    }

    private void handleLoadTest(LoadTestCommand command) {
        try {
            long start = System.currentTimeMillis();
            LoadTestMetrics metrics = LoadTestScenario.builder()
                    .numberOfUsers(command.getNumberOfUsers())
                    .numberOfApplications(command.getNumberOfApplications())
                    .numberOfThreads(command.getNumberOfThreads())
                    .requestPerThread(command.getRequestPerThread())
                    .build()
                    .run(command.getUpdatePeriod(), TimeUnit.SECONDS, CLIClient::printInTable);
            long end = System.currentTimeMillis();
            final long totalCalls = metrics.getSuccessRequestCounter().getCount() + metrics.getExceptionRequestCounter().getCount();
            System.out.println("Total Calls   : " + totalCalls);
            System.out.println("Success Calls : " + metrics.getSuccessRequestCounter().getCount());
            System.out.println("Error Calls   : " + metrics.getExceptionRequestCounter().getCount());
            printTime(end - start);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void printTime(long duration) {
        System.out.println("Execution time : " + duration + " milliseconds\r\n");
    }

    private static void printInTable(List<RankedScore> scores) {
        Object[][] data = new Object[scores.size()][3];
        for (int i = 0; i < scores.size(); i++) {
            RankedScore score = scores.get(i);
            data[i][0] = score.getRank();
            data[i][1] = score.getUserId();
            data[i][2] = score.getScore();
        }
        TextTable textTable = new TextTable(new String[]{"  Rank  ", "  User  ", "  Score  "}, data);
        textTable.printTable(System.out, 3);
        System.out.println();
    }

    private static Object[] toTableValues(String name, Timer timer) {
        return new Object[]{
                name,
                timer.getCount(),
                FMT.format(timer.getSnapshot().getMean()),
                timer.getSnapshot().getMax(),
                timer.getSnapshot().getMin(),
                FMT.format(timer.getMeanRate()),
                FMT.format(timer.getOneMinuteRate()),
                FMT.format(timer.getFiveMinuteRate())
        };
    }

    private static void printInTable(LoadTestMetrics metrics) {
        Object[][] data = new Object[3][];
        data[0] = toTableValues("Submit", metrics.getSubmitTimer());
        data[1] = toTableValues("Search", metrics.getSearchTimer());
        data[2] = toTableValues("GetTopScoreList", metrics.getGetListTimer());
        TextTable textTable = new TextTable(LOAD_TEST_TABLE_HEADERS, data);
        textTable.printTable(System.out, 3);
        System.out.println();
    }

}
