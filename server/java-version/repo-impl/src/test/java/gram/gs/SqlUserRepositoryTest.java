package gram.gs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gram.gs.repository.impl.sql.SqlUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SqlUserRepositoryTest implements UserRepositoryTest {

    private final UserRepositoryTester tester;

    public SqlUserRepositoryTest() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(1);
        config.setJdbcUrl("jdbc:h2:mem:test");
        HikariDataSource dataSource = new HikariDataSource(config);
        tester = new UserRepositoryTester(new SqlUserRepository(dataSource));
    }

    @Test
    @Override
    public void testAdd() throws Exception {
        tester.testAdd();
    }

    @Test
    @Override
    public void testGet() throws Exception {
        tester.testGet();
    }

    @Test
    @Override
    public void testGetOrAdd() throws Exception {
        tester.testGetOrAdd();
    }

    @Test
    @Override
    public void testDoesntExistUser() throws Exception {
        tester.testDoesntExistUser();
    }

    @Test
    @Override
    public void testUserAlreadyException() throws Exception {
        tester.testUserAlreadyException();
    }
}
