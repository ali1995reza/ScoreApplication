package gram.gs.client;

import gram.gs.client.cli.CLIClient;

import java.util.Scanner;

public class RunClient {

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        String host = InputUtils.getString("Server Host", "please enter a valid host", s -> !s.isBlank(), input);
        int port = InputUtils.getInteger("Server Port", "please enter a valid number int [0, 65535]", i -> i > 0 && i < 65535, input);
        int exitCode = new CLIClient(host, port).run(input::nextLine);
        System.exit(exitCode);
    }
}
