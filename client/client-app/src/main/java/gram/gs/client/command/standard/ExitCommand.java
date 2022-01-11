package gram.gs.client.command.standard;

import gram.gs.client.command.annoations.Command;
import gram.gs.client.command.annoations.CommandOption;

@Command("exit")
public class ExitCommand {

    @CommandOption(shortName = "c", longName = "code", required = false, description = "exit code")
    private int code = 0; //default code is 0

    public int getCode() {
        return code;
    }
}
