package gram.gs.client.cli.commands;

import gram.gs.client.command.annoations.Command;
import gram.gs.client.command.annoations.CommandOption;

@Command("get")
public class GetTopScoreCommand {

    @CommandOption(longName = "app", shortName = "a")
    private String applicationId;
    @CommandOption(longName = "offset", shortName = "o", required = false)
    private long offset = 0;
    @CommandOption(longName = "size", shortName = "s", required = false)
    private long size = 10;

    public String getApplicationId() {
        return applicationId;
    }

    public long getOffset() {
        return offset;
    }

    public long getSize() {
        return size;
    }}
