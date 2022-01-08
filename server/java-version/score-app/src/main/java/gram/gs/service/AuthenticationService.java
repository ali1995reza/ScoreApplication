package gram.gs.service;

import gram.gs.exceptions.AuthenticationException;

import java.util.concurrent.TimeUnit;

public interface AuthenticationService {

    /**
     * Create an authentication token
     *
     * @param userId   user identifier
     * @param duration the duration of expiration time
     * @param unit     time unit of expiration duration
     * @return a {@link String} which is a token
     */
    String createToken(String userId, long duration, TimeUnit unit);

    /**
     * Validate and authenticate token
     *
     * @param token token
     * @return user identifier of the token
     * @throws AuthenticationException if any problem occurs when validating like invalid token or token expired
     */
    String validateToken(String token) throws AuthenticationException;

}
