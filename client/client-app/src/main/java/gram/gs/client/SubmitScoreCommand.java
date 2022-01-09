package gram.gs.client;

import gram.gs.client.util.NumericParser;
import org.apache.commons.cli.*;

public class SubmitScoreCommand {

    private final static CommandLineParser PARSER = new DefaultParser();
    private final static Option APP_ID_OPTION = Option.builder().longOpt("app").required().hasArgs().build();
    private final static Option SCORE_OPTION = Option.builder().option("s").longOpt("score").required().hasArg().build();
    private final static Options SUBMIT_SCORE_OPTIONS = new Options()
            .addOption(APP_ID_OPTION)
            .addOption(SCORE_OPTION);

    public static SubmitScoreCommand parse(String... args) {
        try {
            CommandLine commandLine = PARSER.parse(SUBMIT_SCORE_OPTIONS, args);
            final String applicationId = commandLine.getOptionValue(APP_ID_OPTION);
            final Long score = NumericParser.parseLong(commandLine.getOptionValue(SCORE_OPTION), "score");
            return new SubmitScoreCommand(applicationId, score);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    private final String applicationId;
    private final long score;

    public SubmitScoreCommand(String applicationId, long score) {
        this.applicationId = applicationId;
        this.score = score;
    }

    public long getScore() {
        return score;
    }

    public String getApplicationId() {
        return applicationId;
    }
}
