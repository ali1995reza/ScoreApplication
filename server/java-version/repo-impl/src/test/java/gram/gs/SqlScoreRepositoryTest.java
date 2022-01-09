package gram.gs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gram.gs.model.RankedScore;
import gram.gs.repository.impl.memory.InMemoryScoreRepository;
import gram.gs.repository.impl.sql.SqlScoreRepository;
import gram.gs.repository.impl.sql.SqlUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SqlScoreRepositoryTest {

    private final Random random = new Random();
    private final SqlScoreRepository scoreRepository;

    public SqlScoreRepositoryTest() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(1);
        config.setJdbcUrl("jdbc:h2:mem:test");
        HikariDataSource dataSource = new HikariDataSource(config);
        scoreRepository = new SqlScoreRepository(dataSource);
    }

    @BeforeEach
    private void clear() {
        scoreRepository.clear();
    }


    @Test
    public void testSubmitScore() {
        for (int i = 0; i < 1000; i++) {
            scoreRepository.save(TestUtils.newId(), TestUtils.newId(), random.nextInt(100));
        }
    }

    @Test
    public void testSubmitScoreTwice() {
        final String userId = TestUtils.getId("user");
        final String appId = TestUtils.getId("app");

        RankedScore score = scoreRepository.save(userId, appId, 20);
        assertEquals(1, score.getRank());
        score = scoreRepository.save(TestUtils.newId(), appId, 30);
        assertEquals(1, score.getRank());
        score = scoreRepository.get(userId, appId);
        assertEquals(2, score.getRank());
        score = scoreRepository.save(userId, appId, 40);
        assertEquals(1, score.getRank());
    }


    @Test
    public void testGetList() {
        for (int i = 0; i < 1000; i++) {
            RankedScore score = scoreRepository.save(TestUtils.getId(i), TestUtils.getId(i / 100), (i % 100) + 1);
            assertEquals(1, score.getRank());
        }
        for (int i = 0; i < 10; i++) {
            List<RankedScore> scores = scoreRepository.get(TestUtils.getId(i), 10, 20);
            int count = 0;
            for (RankedScore score : scores) {
                assertEquals(100 - 10 - count, score.getScore());
                assertEquals(10 + 1 + count, score.getRank());
                Assertions.assertEquals(TestUtils.getId(((i + 1) * 100) - 1 - 10 - count), score.getUserId());
                ++count;
            }
        }
    }

}
