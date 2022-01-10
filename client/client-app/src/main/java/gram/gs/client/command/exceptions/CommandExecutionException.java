package gram.gs.client.command.exceptions;

public class CommandExecutionException extends CommandException {
    public CommandExecutionException(Throwable e) {
        super(e);
    }

    public CommandExecutionException(String e) {
        super(e);
    }
}
