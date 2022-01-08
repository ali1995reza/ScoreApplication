package gram.gs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gram.gs.exceptions.UserAlreadyExistsException;
import gram.gs.model.User;
import gram.gs.repository.impl.memory.InMemoryUserRepository;
import gram.gs.repository.impl.sql.SqlUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static gram.gs.TestUtils.newId;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SqlUserRepositoryTest {


    private final SqlUserRepository userRepository;

    public SqlUserRepositoryTest() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(1);
        config.setJdbcUrl("jdbc:h2:mem:test");
        HikariDataSource dataSource = new HikariDataSource(config);
        userRepository = new SqlUserRepository(dataSource);
    }

    @BeforeEach
    private void clearRepository() {
        userRepository.clear();
    }

    @Test
    public void testAdd() throws Exception {
        String id = newId();
        User user = userRepository.add(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void testGet() throws Exception {
        String id = newId();
        userRepository.add(id);
        User user = userRepository.get(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void testGetOrAdd() throws Exception {
        String id = newId();
        User user = userRepository.addOrGet(id);
        assertEquals(id, user.getId());
        user = userRepository.addOrGet(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void testDoesntExistUser() throws Exception {
        User user = userRepository.get(newId());
        assertNull(user);
    }

    @Test
    public void testUserAlreadyException() throws Exception {
        assertThrows(UserAlreadyExistsException.class, () -> {
            String id = newId();
            userRepository.add(id);
            userRepository.add(id);
        });
    }
}
