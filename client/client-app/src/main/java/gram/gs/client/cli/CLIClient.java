package gram.gs.client.cli;

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
import gram.gs.client.load.LoadTestScenario;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class CLIClient {

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
                System.out.println("ERROR : " + e.getMessage());
                System.out.println();
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
        System.out.println("Execution time : " + (end - start) + " milliseconds");
        System.out.println();
    }

    private void handleLogout(LogoutCommand command) throws Exception {
        long start = System.currentTimeMillis();
        this.token = null;
        this.currentUser = null;
        long end = System.currentTimeMillis();
        System.out.println("Logout !");
        System.out.println("Execution time : " + (end - start) + " milliseconds");
        System.out.println();
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
        System.out.println("Execution time : " + (end - start) + " milliseconds");
        System.out.println();
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
        System.out.println("Execution time : " + (end - start) + " milliseconds");
        System.out.println();
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
        System.out.println("Execution time : " + (end - start) + " milliseconds");
        System.out.println();
    }

    private void handleLoadTest(LoadTestCommand command) {
        try {
            LoadTestScenario.builder()
                    .numberOfUsers(command.getNumberOfUsers())
                    .numberOfApplications(command.getNumberOfApplications())
                    .numberOfThreads(command.getNumberOfThreads())
                    .requestPerThread(command.getRequestPerThread())
                    .build()
                    .run(command.getUpdatePeriod(), TimeUnit.SECONDS, (metrics) -> {
                        System.out.println(metrics.getSuccessRequestCounter().getCount());
                    });
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
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
        textTable.printTable(System.out, 5);
        System.out.println();
    }

}
