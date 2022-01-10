package gram.gs.client.command.parser;

public interface CommandHandler<T> {

    void handle(T t) throws Throwable;
}
