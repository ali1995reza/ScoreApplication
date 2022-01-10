package gram.gs.client.cli;

import dnl.utils.text.table.TextTable;
import gram.gs.client.abs.ScoreApplicationClient;
import gram.gs.client.abs.dto.ClientToken;
import gram.gs.client.abs.dto.RankedScore;
import gram.gs.client.impl.HttpScoreApplicationClient;
import gram.gs.client.util.ConsoleUtils;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.util.List;
import java.util.Scanner;

public class CLIClient {

    public static void main(String[] args) {
        CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();
        httpClient.start();
        ScoreApplicationClient client = new HttpScoreApplicationClient(httpClient, host, port);

        final Scanner input = new Scanner(System.in);

        ClientToken token = null;
        String userId = null;

        while (true) {
            printInterceptorLine(userId);
            String[] commandLine = input.nextLine().split("\\s+");
            String commandName = commandLine[0];
            try {
                if (commandName.equals("login")) {
                    LoginCommand command = LoginCommand.parse(chunk(commandLine, 1));
                    token = client.login(command.getUserId()).get();
                    System.out.println("logged in as : " + command.getUserId());
                    userId = command.getUserId();
                } else if (commandName.equals("submit")) {
                    SubmitScoreCommand command = SubmitScoreCommand.parse(chunk(commandLine, 1));
                    if (token == null) {
                        throw new IllegalStateException("first login !");
                    }
                    RankedScore score =
                            client.submitScore(token.getToken(), command.getApplicationId(), command.getScore()).get();
                    printInTable(List.of(score));
                } else if (commandName.equals("get")) {
                    GetTopScoreCommand command = GetTopScoreCommand.parse(chunk(commandLine, 1));
                    List<RankedScore> scores =
                            client.getTopScoreList(command.getApplicationId(), command.getOffset(), command.getSize()).get();
                    printInTable(scores);
                } else if (commandName.equals("search")) {
                    SearchScoreCommand command = SearchScoreCommand.parse(chunk(commandLine, 1));
                    List<RankedScore> scores =
                            client.searchScoreList(command.getUserId(), command.getApplicationId(), command.getTop(), command.getBottom()).get();
                    printInTable(scores);
                } else if (commandName.equals("logout")) {
                    if(token == null) {
                        throw new IllegalStateException("you are not logged in");
                    }
                    token = null;
                    userId = null;
                } else if (commandName.equals("clear") || commandName.equals("cls")) {
                    ConsoleUtils.clear();
                }
            } catch (Exception e) {
                System.out.println("ERROR : " + e.getMessage());
            }
        }
    }

    private static String[] chunk(String[] strings, int offset, int len) {
        String[] chunk = new String[len];
        System.arraycopy(strings, offset, chunk, 0, len);
        return chunk;
    }

    private static String[] chunk(String[] strings, int offset) {
        return chunk(strings, offset, strings.length - offset);
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

    private static void printInterceptorLine(String userId) {
        if (userId == null) {
            System.out.print("- >> ");
        } else {
            System.out.print(userId + " >> ");
        }
    }
}
