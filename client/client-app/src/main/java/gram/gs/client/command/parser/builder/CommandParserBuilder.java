package gram.gs.client.command.parser.builder;

import gram.gs.client.assertion.Assert;
import gram.gs.client.command.parser.CommandParser;

import java.util.*;

public class CommandParserBuilder {

    public static CommandParserBuilder builder() {
        return new CommandParserBuilder();
    }

    private final List<Class> commandClasses = new ArrayList<>();

    private CommandParserBuilder() {
    }

    public synchronized CommandParserBuilder add(Class commandClass, Class... others) {
        check(commandClass);
        if (others != null) {
            for (Class cls : others) {
                check(cls);
            }
            for (Class cls : others) {
                commandClasses.add(cls);
            }
        }

        commandClasses.add(commandClass);

        return this;
    }

    public synchronized CommandParserBuilder clear() {
        commandClasses.clear();
        return this;
    }

    public synchronized CommandParser build() {
        Map<String, ParsedCommand> parsedCommands = new HashMap<>();
        for (Class clazz : commandClasses) {
            String commandName = CommandAnnotationUtil.getCommandName(clazz);
            Assert.isFalse(parsedCommands.containsKey(commandName), () -> new IllegalStateException("duplicate command name [" + commandName + "]"));
            List<OptionFieldPair> options = CommandAnnotationUtil.getCommandOptions(clazz);
            parsedCommands.put(commandName, new ParsedCommand(commandName, clazz, options));
        }
        return new AnnotationBaseCommandParser(Collections.unmodifiableMap(parsedCommands));
    }

    private void check(Class commandClass) {
        Assert.isNotNull(commandClass, () -> new NullPointerException("null command class provided"));
        Assert.isFalse(commandClasses.contains(commandClass), () -> new IllegalStateException("class [" + commandClass + "] already exists"));
    }
}
