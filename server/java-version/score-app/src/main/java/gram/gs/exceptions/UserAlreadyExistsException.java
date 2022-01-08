package gram.gs.exceptions;

public class UserAlreadyExistsException extends ScoreApplicationException {

    public final static String ID = "USER_ALREADY_EXISTS";

    public UserAlreadyExistsException(String e) {
        super(ID, e);
    }

    public UserAlreadyExistsException() {
        super(ID, "user already exists");
    }
}
