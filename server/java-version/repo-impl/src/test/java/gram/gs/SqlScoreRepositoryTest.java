package gram.gs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gram.gs.repository.impl.sql.SqlScoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SqlScoreRepositoryTest implements ScoreRepositoryTest {

    private final ScoreRepositoryTester tester;

    public SqlScoreRepositoryTest() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(1);
        config.setJdbcUrl("jdbc:h2:mem:test");
        HikariDataSource dataSource = new HikariDataSource(config);
        this.tester = new ScoreRepositoryTester(new SqlScoreRepository(dataSource));
    }

    @Test
    @Override
    public void testSubmitScore() {
        tester.testSubmitScore();
    }

    @Test
    @Override
    public void testSubmitScoreTwice() {
        tester.testSubmitScoreTwice();
    }

    @Test
    @Override
    public void testGetList() {
        tester.testGetList();
    }

    @Test
    @Override
    public void testSearchScoreList() {
        tester.testSearchScoreList();
    }
}
