package gram.gs.repository.impl.memory;

import gram.gs.assertion.Assert;
import gram.gs.exceptions.UserAlreadyExistsException;
import gram.gs.model.User;
import gram.gs.repository.UserRepository;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {

    private final ConcurrentHashMap<String, User> usres;

    public InMemoryUserRepository() {
        usres = new ConcurrentHashMap<>();
    }

    @Override
    public User get(String id) {
        return usres.get(id);
    }

    @Override
    public User add(String id) throws UserAlreadyExistsException {
        Assert.isFalse(usres.containsKey(id), UserAlreadyExistsException::new);
        return usres.computeIfAbsent(id, User::new);
    }

    @Override
    public User addOrGet(String id) {
        return usres.computeIfAbsent(id, User::new);
    }

    @Override
    public void clear() {
        usres.clear();
    }
}
