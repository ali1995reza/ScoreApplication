package gram.gs.exceptions;

public class InvalidParametersException extends ScoreApplicationException {

    public final static String ID = "INVALID_PARAMETERS";

    public InvalidParametersException(String e) {
        super(ID, e);
    }

    public InvalidParametersException() {
        this("invalid parameters");
    }
}
