package gram.gs;

import gram.gs.exceptions.UserAlreadyExistsException;
import gram.gs.model.User;
import gram.gs.repository.impl.memory.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static gram.gs.TestUtils.newId;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InMemoryUserRepositoryTest implements UserRepositoryTest {

    private final UserRepositoryTester tester;

    public InMemoryUserRepositoryTest() {
        tester = new UserRepositoryTester(new InMemoryUserRepository());
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
