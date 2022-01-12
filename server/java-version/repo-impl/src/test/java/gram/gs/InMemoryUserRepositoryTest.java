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
public class InMemoryUserRepositoryTest {


    private final InMemoryUserRepository userRepository;

    public InMemoryUserRepositoryTest() {
        userRepository = new InMemoryUserRepository();
    }

    private void clear() {
        userRepository.clear();
    }

    @Test
    public void testAdd() throws Exception {
        clear();
        String id = newId();
        User user = userRepository.add(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void testGet() throws Exception {
        clear();
        String id = newId();
        userRepository.add(id);
        User user = userRepository.get(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void testGetOrAdd() throws Exception {
        clear();
        String id = newId();
        User user = userRepository.addOrGet(id);
        assertEquals(id, user.getId());
        user = userRepository.addOrGet(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void testDoesntExistUser() throws Exception {
        clear();
        User user = userRepository.get(newId());
        assertNull(user);
    }

    @Test
    public void testUserAlreadyException() throws Exception {
        clear();
        assertThrows(UserAlreadyExistsException.class, () -> {
            String id = newId();
            userRepository.add(id);
            userRepository.add(id);
        });
    }
}
