package gram.gs.client.cli;

import gram.gs.client.util.NumericParser;
import org.apache.commons.cli.*;

public class SearchScoreCommand {

    private final static CommandLineParser PARSER = new DefaultParser();
    private final static Option USER_ID_OPTION = Option.builder().option("u").longOpt("user").required().hasArgs().build();
    private final static Option APP_ID_OPTION = Option.builder().longOpt("app").required().hasArgs().build();
    private final static Option TOP_OPTION = Option.builder().option("t").longOpt("top").hasArg().build();
    private final static Option BOTTOM_OPTION = Option.builder().option("b").longOpt("bottom").hasArg().build();
    private final static Options GET_TOP_SCORE_OPTIONS = new Options()
            .addOption(USER_ID_OPTION)
            .addOption(APP_ID_OPTION)
            .addOption(TOP_OPTION)
            .addOption(BOTTOM_OPTION);

    public static SearchScoreCommand parse(String... args) {
        try {
            CommandLine commandLine = PARSER.parse(GET_TOP_SCORE_OPTIONS, args);
            final String userId = commandLine.getOptionValue(USER_ID_OPTION);
            final String applicationId = commandLine.getOptionValue(APP_ID_OPTION);
            int top = NumericParser.parseInteger(commandLine.getOptionValue(TOP_OPTION), 0, "top");
            int bottom = NumericParser.parseInteger(commandLine.getOptionValue(BOTTOM_OPTION), 0, "bottom");
            return new SearchScoreCommand(userId, applicationId, top, bottom);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }



    private final String userId;
    private final String applicationId;
    private final int top;
    private final int bottom;

    public SearchScoreCommand(String userId, String applicationId, int top, int bottom) {
        this.userId = userId;
        this.applicationId = applicationId;
        this.top = top;
        this.bottom = bottom;
    }

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
