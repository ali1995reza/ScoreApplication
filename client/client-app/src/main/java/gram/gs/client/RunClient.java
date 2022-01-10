package gram.gs.client;

import gram.gs.client.cli.CLIClient;

import java.util.Scanner;

public class RunClient {

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.print("Hostname : ");
        String host = input.nextLine();
        System.out.print("Port : ");
        int port = Integer.parseInt(input.nextLine());
        int exitCode = new CLIClient(host, port).run(input::nextLine);
        System.exit(exitCode);
    }
}
