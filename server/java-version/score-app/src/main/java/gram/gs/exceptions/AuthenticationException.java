package gram.gs.exceptions;

public class AuthenticationException extends ScoreApplicationException {
    public AuthenticationException(String id, String e) {
        super(id, e);
    }
}
