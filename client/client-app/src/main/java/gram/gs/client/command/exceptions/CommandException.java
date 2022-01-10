package gram.gs.client.command.exceptions;

public class CommandException extends Exception {

    public CommandException(Throwable e) {
        super(e);
    }

    public CommandException(String e) {
        super(e);
    }

}
