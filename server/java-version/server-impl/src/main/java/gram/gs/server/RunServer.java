package gram.gs.server;

import com.auth0.jwt.algorithms.Algorithm;
import gram.gs.ScoreApplication;
import gram.gs.repository.impl.memory.InMemoryScoreRepository;
import gram.gs.repository.impl.memory.InMemoryUserRepository;
import gram.gs.server.abs.ScoreAppHttpServer;
import gram.gs.server.impl.javalin.JavalinScoreAppHttpServer;
import gram.gs.service.impl.jwt.JwtAuthenticationToken;

public class RunServer {

    public static void main(String[] args) {
        ScoreAppHttpServer server = new JavalinScoreAppHttpServer()
                .application(new ScoreApplication(new InMemoryUserRepository(), new InMemoryScoreRepository(), new JwtAuthenticationToken(
                        Algorithm.HMAC512("secret")
                )))
                .numberOfHandlerThreads(1)
                .host("localhost")
                .port(8080)
                .start();
    }
}
