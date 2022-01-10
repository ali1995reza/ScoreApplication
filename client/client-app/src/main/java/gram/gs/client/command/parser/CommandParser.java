package gram.gs.client.command.parser;

import gram.gs.client.command.exceptions.CommandException;

public interface CommandParser {

    Object parse(String command) throws CommandException;

    <T> CommandParser setCommandHandler(Class<T> type, CommandHandler<T> handler);

}
