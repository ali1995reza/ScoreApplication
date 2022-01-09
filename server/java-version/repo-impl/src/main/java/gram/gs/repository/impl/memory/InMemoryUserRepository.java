package gram.gs.repository.impl.memory;

import gram.gs.assertion.Assert;
import gram.gs.exceptions.UserAlreadyExistsException;
import gram.gs.model.User;
import gram.gs.repository.impl.ValidatedUserRepository;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository extends ValidatedUserRepository {

    private final ConcurrentHashMap<String, User> users;

    public InMemoryUserRepository() {
        users = new ConcurrentHashMap<>();
    }

    @Override
    public User doGet(String id) {
        return users.get(id);
    }

    @Override
    public User doAdd(String id) throws UserAlreadyExistsException {
        Assert.isFalse(users.containsKey(id), UserAlreadyExistsException::new);
        return users.computeIfAbsent(id, User::new);
    }

    @Override
    public User doAddOrGet(String id) {
        return users.computeIfAbsent(id, User::new);
    }

    @Override
    public void clear() {
        users.clear();
    }
}
