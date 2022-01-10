package gram.gs.client.command;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface CommandParser<T> {

    void pars(String[] command, BiConsumer<T, Throwable> handler);

}
