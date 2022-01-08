package gram.gs.exceptions;

public class InvalidApplicationIdFormatException extends ScoreApplicationException {

    public final static String ID = "INVALID_APPLICATION_ID";

    public InvalidApplicationIdFormatException(String e) {
        super(ID, e);
    }

    public InvalidApplicationIdFormatException() {
        this("application id format is invalid");
    }
}
