package gram.gs.exceptions;

public class ScoreNotFoundException extends ScoreApplicationException {

    public final static String ID = "SCORE_NOT_FOUND";

    public ScoreNotFoundException(String e) {
        super(ID, e);
    }

    public ScoreNotFoundException() {
        this("requested score not found");
    }
}
