package gram.gs.client.cli.commands;

import gram.gs.client.command.annoations.Command;
import gram.gs.client.command.annoations.CommandOption;

@Command("submit")
public class SubmitScoreCommand {

    @CommandOption(longName = "app", shortName = "a")
    private String applicationId;
    @CommandOption(longName = "score", shortName = "s")
    private long score;

    public long getScore() {
        return score;
    }

    public String getApplicationId() {
        return applicationId;
    }

}
