package gram.gs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gram.gs.repository.impl.memory.InMemoryScoreRepository;
import gram.gs.repository.impl.sql.SqlScoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InMemoryScoreRepositoryTest implements ScoreRepositoryTest {

    private final ScoreRepositoryTester tester;

    public InMemoryScoreRepositoryTest() {
        this.tester = new ScoreRepositoryTester(new InMemoryScoreRepository());
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
