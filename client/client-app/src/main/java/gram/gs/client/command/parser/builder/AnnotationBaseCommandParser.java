package gram.gs.client.command.parser.builder;

import gram.gs.client.assertion.Assert;
import gram.gs.client.command.exceptions.*;
import gram.gs.client.command.parser.CommandHandler;
import gram.gs.client.command.parser.CommandParser;
import gram.gs.client.command.standard.HelpCommand;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationBaseCommandParser implements CommandParser {

    private final static HelpFormatter HELP_FORMATTER = new HelpFormatter();

    private final CommandLineParser commandLineParser;
    private final Map<String, ParsedCommand> commandMap;
    private final ConcurrentHashMap<Class, CommandHandler<? extends Object>> handlers;
    private final String helpString;

    public AnnotationBaseCommandParser(Map<String, ParsedCommand> commandMap) {
        this.commandLineParser = new DefaultParser();
        this.commandMap = commandMap;
        this.handlers = new ConcurrentHashMap<>();
        this.helpString = getHelpString();
    }

    @Override
    public Object parse(String command) throws CommandException {
        Assert.isNotBlank(command, EmptyCommandException::new);
        String[] parts = command.split("\\s+");
        String commandName = parts[0];
        ParsedCommand parsedCommand = commandMap.get(commandName);
        Assert.isNotNull(parsedCommand, () -> CommandNotFoundException.of(commandName));
        String[] args = chunk(parts, 1);
        CommandLine commandLine = null;
        Object commandObject = null;
        try {
            commandLine = commandLineParser.parse(parsedCommand.getOptions(), args);
            commandObject = createCommand(parsedCommand, commandLine);
        } catch (Exception e) {
            throw new CommandParseException(e);
        }


        CommandHandler handler = handlers.getOrDefault(commandObject.getClass(), o -> {
        });

        try {
            if (commandObject instanceof HelpCommand) {
                printHelp();
            }
            handler.handle(commandObject);
        } catch (Throwable e) {
            throw new CommandExecutionException(e);
        }
        return commandObject;
    }

    @Override
    public <T> CommandParser setCommandHandler(Class<T> type, CommandHandler<T> handler) {
        Assert.isNotNull(type, () -> new NullPointerException("command type is null"));
        Assert.isNotNull(handler, () -> new NullPointerException("handler is null"));
        handlers.put(type, handler);
        return this;
    }

    private String getHelpString() {
        String help = "";
        for (ParsedCommand command : commandMap.values()) {
            if (command.getType() == HelpCommand.class) {
                continue;
            }
            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            HELP_FORMATTER.printUsage(writer, 1000, command.getCommandName(), command.getOptions());
            writer.flush();
            writer.close();
            String usage = stringWriter.toString().substring(7);
            stringWriter = new StringWriter();
            writer = new PrintWriter(stringWriter);
            HELP_FORMATTER.printHelp(writer, 1000, usage, "", command.getOptions(), 5, 0, "\n");
            writer.flush();
            writer.close();
            help += stringWriter.toString();
        }
        return help;
    }

    private void printHelp() {
        System.out.println(helpString);
    }

    private Object createCommand(ParsedCommand command, CommandLine commandLine) throws Exception {
        Object o = command.getType().getConstructor().newInstance();
        for (OptionFieldPair op : command.getFields()) {
            if (commandLine.hasOption(op.getOption())) {
                if (op.getOption().hasArg()) {
                    op.set(o, commandLine.getOptionValue(op.getOption()));
                } else {
                    op.set(o, "0");
                }
            } else if (!op.getOption().hasArg()) {
                op.set(o, "1");
            }
        }
        return o;
    }

    private static String[] chunk(String[] strings, int offset, int len) {
        String[] chunk = new String[len];
        System.arraycopy(strings, offset, chunk, 0, len);
        return chunk;
    }

    private static String[] chunk(String[] strings, int offset) {
        return chunk(strings, offset, strings.length - offset);
    }
}
