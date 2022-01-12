package gram.gs;

import gram.gs.exceptions.UserAlreadyExistsException;
import gram.gs.model.User;
import gram.gs.repository.UserRepository;

import static gram.gs.TestUtils.newId;
import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTester implements UserRepositoryTest {

    private final UserRepository userRepository;

    public UserRepositoryTester(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    private void clear() {
        userRepository.clear();
    }

    @Override
    public void testAdd() throws Exception {
        clear();
        String id = newId();
        User user = userRepository.add(id);
        assertEquals(id, user.getId());
    }

    @Override
    public void testGet() throws Exception {
        clear();
        String id = newId();
        userRepository.add(id);
        User user = userRepository.get(id);
        assertEquals(id, user.getId());
    }

    @Override
    public void testGetOrAdd() throws Exception {
        clear();
        String id = newId();
        User user = userRepository.addOrGet(id);
        assertEquals(id, user.getId());
        user = userRepository.addOrGet(id);
        assertEquals(id, user.getId());
    }

    @Override
    public void testDoesntExistUser() throws Exception {
        clear();
        User user = userRepository.get(newId());
        assertNull(user);
    }

    @Override
    public void testUserAlreadyException() throws Exception {
        clear();
        assertThrows(UserAlreadyExistsException.class, () -> {
            String id = newId();
            userRepository.add(id);
            userRepository.add(id);
        });
    }
}
