package gram.gs.client.cli.commands;

import gram.gs.client.command.annoations.Command;
import gram.gs.client.command.annoations.CommandOption;

@Command("search")
public class SearchScoreCommand {

    @CommandOption(longName = "user", shortName = "u")
    private String userId;
    @CommandOption(longName = "app", shortName = "a")
    private String applicationId;
    @CommandOption(longName = "top", shortName = "t", required = false)
    private int top = 1;
    @CommandOption(longName = "bottom", shortName = "b", required = false)
    private int bottom = 1;

    public String getUserId() {
        return userId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }
}
