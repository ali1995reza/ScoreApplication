package gram.gs.client.cli.commands;

import gram.gs.client.command.annoations.Command;
import gram.gs.client.command.annoations.CommandOption;

@Command("search")
public class SearchScoreCommand {

    @CommandOption(longName = "user", shortName = "u", description = "target user to search in application score list")
    private String userId;
    @CommandOption(longName = "app", shortName = "a", description = "target application to search")
    private String applicationId;
    @CommandOption(longName = "top", shortName = "t", required = false, description = "number of top scores to fetch")
    private int top = 1;
    @CommandOption(longName = "bottom", shortName = "b", required = false, description = "number of bottom scores to fetch")
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
