package gram.gs.client.cli.commands;

import gram.gs.client.command.annoations.Command;
import gram.gs.client.command.annoations.CommandOption;

@Command("login")
public class LoginCommand {

    @CommandOption(longName = "user", shortName = "u")
    private String userId;

    public String getUserId() {
        return userId;
    }

}
