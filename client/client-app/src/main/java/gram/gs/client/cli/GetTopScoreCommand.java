package gram.gs.client.cli;

import gram.gs.client.util.NumericParser;
import org.apache.commons.cli.*;

public class GetTopScoreCommand {
    private final static CommandLineParser PARSER = new DefaultParser();
    private final static Option APP_ID_OPTION = Option.builder().longOpt("app").required().hasArgs().build();
    private final static Option OFFSET_OPTION = Option.builder().option("o").longOpt("offset").hasArg().build();
    private final static Option SIZE_OPTION = Option.builder().option("s").longOpt("size").hasArg().build();
    private final static Options GET_TOP_SCORE_OPTIONS = new Options()
            .addOption(APP_ID_OPTION)
            .addOption(OFFSET_OPTION)
            .addOption(SIZE_OPTION);

    public static GetTopScoreCommand parse(String... args) {
        try {
            CommandLine commandLine = PARSER.parse(GET_TOP_SCORE_OPTIONS, args);
            final String applicationId = commandLine.getOptionValue(APP_ID_OPTION);
            final Long offset = NumericParser.parseLong(commandLine.getOptionValue(OFFSET_OPTION), 0L, "offset");
            final Long size = NumericParser.parseLong(commandLine.getOptionValue(SIZE_OPTION), 0L, "size");
            return new GetTopScoreCommand(applicationId, offset, size);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }


    private final String applicationId;
    private final long offset;
    private final long size;

    public GetTopScoreCommand(String applicationId, long offset, long size) {
        this.applicationId = applicationId;
        this.offset = offset;
        this.size = size;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public long getOffset() {
        return offset;
    }

    public long getSize() {
        return size;
    }
}
