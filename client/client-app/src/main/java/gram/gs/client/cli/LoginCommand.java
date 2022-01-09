package gram.gs.client.cli;

import org.apache.commons.cli.*;

public class LoginCommand {
    private final static CommandLineParser PARSER = new DefaultParser();
    private final static Option USER_ID_OPTION = Option.builder().option("u").longOpt("user").required().hasArgs().build();
    private final static Options LOGIN_OPTIONS = new Options()
            .addOption(USER_ID_OPTION);

    public static LoginCommand parse(String... args) {
        try {
            CommandLine commandLine = PARSER.parse(LOGIN_OPTIONS, args);
            final String userId = commandLine.getOptionValue(USER_ID_OPTION);
            return new LoginCommand(userId);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }


    private final String userId;

    public LoginCommand(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
