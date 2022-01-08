package gram.gs.service;

import gram.gs.exceptions.AuthenticationException;

import java.util.concurrent.TimeUnit;

public interface AuthenticationService {

    String createToken(String userId, long duration, TimeUnit unit);

    String validateToken(String token) throws AuthenticationException;

}
