package gram.gs.exceptions;

public class AuthenticationTokenExpiredException extends AuthenticationException {

    public final static String ID = "TOKEN_EXPIRED";

    public AuthenticationTokenExpiredException(String e) {
        super(ID, e);
    }

    public AuthenticationTokenExpiredException() {
        this("token valid time expired");
    }
}
