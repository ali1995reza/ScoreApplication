package gram.gs.server;

import com.auth0.jwt.algorithms.Algorithm;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gram.gs.ScoreApplication;
import gram.gs.repository.ScoreRepository;
import gram.gs.repository.UserRepository;
import gram.gs.repository.impl.memory.InMemoryScoreRepository;
import gram.gs.repository.impl.memory.InMemoryUserRepository;
import gram.gs.repository.impl.sql.SqlScoreRepository;
import gram.gs.repository.impl.sql.SqlUserRepository;
import gram.gs.server.impl.javalin.JavalinScoreAppHttpServer;
import gram.gs.service.impl.jwt.JwtAuthenticationToken;

import java.util.List;
import java.util.Scanner;

public class RunServer {

    private final static String H2DB = "H2DB";
    private final static String MEM = "MEM";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String host = InputUtils.getString("Server Host", "please enter a valid host", s -> !s.isBlank(), input);
        int port = InputUtils.getInteger("Server Port", "please enter a valid number int [0, 65535]", i -> i > 0 && i < 65535, input);
        int numberOfThreads = InputUtils.getInteger("Number of IO handler Threads", "please enter a positive number", i -> i > 0, input);
        String db = InputUtils.getValidString("Enter Database model [MEM,H2DB(SQL base)]", List.of(MEM, H2DB), input);

        UserRepository userRepository = null;
        ScoreRepository scoreRepository = null;

        if (db.equals(H2DB)) {
            int poolSize = InputUtils.getInteger("H2DB connection pool size", "please enter a positive number", i -> i > 0, input);
            HikariConfig config = new HikariConfig();
            config.setMaximumPoolSize(poolSize);
            config.setJdbcUrl("jdbc:h2:mem:test");
            HikariDataSource dataSource = new HikariDataSource(config);
            userRepository = new SqlUserRepository(dataSource);
            scoreRepository = new SqlScoreRepository(dataSource);
        } else {
            userRepository = new InMemoryUserRepository();
            scoreRepository = new InMemoryScoreRepository();
        }

        new JavalinScoreAppHttpServer()
                .application(
                        new ScoreApplication(userRepository, scoreRepository,
                                new JwtAuthenticationToken(Algorithm.HMAC512("secret")))
                )
                .host(host)
                .port(port)
                .numberOfHandlerThreads(numberOfThreads)
                .start();
    }
}
