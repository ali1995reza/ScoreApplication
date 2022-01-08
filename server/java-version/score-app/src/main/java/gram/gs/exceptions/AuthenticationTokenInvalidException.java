package gram.gs.exceptions;

public class AuthenticationTokenInvalidException extends AuthenticationException {

    public final static String ID = "INVALID_TOKEN";

    public AuthenticationTokenInvalidException(String e) {
        super(ID, e);
    }

    public AuthenticationTokenInvalidException() {
        this("authentication token is invalid");
    }
}
