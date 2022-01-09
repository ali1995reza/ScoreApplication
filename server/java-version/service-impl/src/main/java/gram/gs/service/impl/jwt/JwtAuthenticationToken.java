package gram.gs.service.impl.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import gram.gs.assertion.Assert;
import gram.gs.exceptions.AuthenticationException;
import gram.gs.exceptions.AuthenticationTokenExpiredException;
import gram.gs.exceptions.AuthenticationTokenInvalidException;
import gram.gs.service.ValidatedAuthenticationService;

import java.security.spec.ECParameterSpec;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JwtAuthenticationToken extends ValidatedAuthenticationService {

    private final static String ISSUER = "GramGames";
    private final static String USER_ID = "userId";
    private final static String EXPIRATION_TIME = "expirationTime";
    private final static String RANDOM_VALUE = "__rand__";

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtAuthenticationToken(Algorithm algorithm) {
        Assert.isNotNull(algorithm, () -> new NullPointerException("algorithm is null"));
        this.algorithm = algorithm;
        this.verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
    }

    @Override
    protected String doCreateToken(String userId, long duration, TimeUnit timeUnit) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim(USER_ID, userId)
                .withClaim(EXPIRATION_TIME,
                        Instant.now().plusMillis(timeUnit.toMillis(duration)).toEpochMilli())
                .withClaim(RANDOM_VALUE, UUID.randomUUID().toString())
                .sign(algorithm);
    }

    @Override
    public String validateToken(String token) throws AuthenticationException {
        try {
            DecodedJWT jwt = verifier.verify(token);
            Instant expirationTime = Instant.ofEpochMilli(jwt.getClaim(EXPIRATION_TIME).asLong());
            if (Instant.now().isAfter(expirationTime)) {
                throw new AuthenticationTokenExpiredException();
            }
            return jwt.getClaim(USER_ID).asString();
        } catch (JWTVerificationException e) {
            throw new AuthenticationTokenInvalidException();
        }
    }
}
