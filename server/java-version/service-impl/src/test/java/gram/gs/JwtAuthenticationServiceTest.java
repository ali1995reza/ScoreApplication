package gram.gs;

import com.auth0.jwt.algorithms.Algorithm;
import gram.gs.exceptions.AuthenticationTokenExpiredException;
import gram.gs.exceptions.AuthenticationTokenInvalidException;
import gram.gs.service.AuthenticationService;
import gram.gs.service.impl.jwt.JwtAuthenticationToken;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static gram.gs.TestUtils.newId;
import static org.junit.jupiter.api.Assertions.*;

public class JwtAuthenticationServiceTest {
    private final AuthenticationService authenticationService;

    public JwtAuthenticationServiceTest() {
        this.authenticationService = new JwtAuthenticationToken(Algorithm.HMAC512("some-secret"));
    }

    @Test
    public void testAuth() throws Exception {
        String userId = newId();
        String token = authenticationService.createToken(userId, 20, TimeUnit.SECONDS);
        assertNotNull(token);
        assertFalse(token.isBlank());
        String tokenUserId = authenticationService.validateToken(token);
        assertEquals(userId, tokenUserId);
    }

    @Test
    public void testTokenExpiredException() {
        assertThrows(AuthenticationTokenExpiredException.class, () -> {
            String userId = newId();
            String token = authenticationService.createToken(userId, 1, TimeUnit.MILLISECONDS);
            Thread.sleep(1);
            authenticationService.validateToken(token);
        });
    }

    @Test
    public void testInvalidTokenException() {
        assertThrows(AuthenticationTokenInvalidException.class, () ->
                authenticationService.validateToken("invalid-token")
        );
    }
}
