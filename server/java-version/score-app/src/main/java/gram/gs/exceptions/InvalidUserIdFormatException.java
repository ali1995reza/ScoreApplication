package gram.gs.exceptions;

public class InvalidUserIdFormatException extends ScoreApplicationException {

    public final static String ID = "INVALID_USER_ID";

    public InvalidUserIdFormatException(String e) {
        super(ID, e);
    }

    public InvalidUserIdFormatException() {
        this("user id format is invalid");
    }
}
