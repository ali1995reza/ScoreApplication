package gram.gs.repository.impl;

import gram.gs.assertion.Assert;
import gram.gs.exceptions.UserAlreadyExistsException;
import gram.gs.model.User;
import gram.gs.repository.UserRepository;

public abstract class ValidatedUserRepository implements UserRepository {

    @Override
    public final User get(String id) {
        Assert.isNotNull(id, () -> new NullPointerException("id is null"));
        return doGet(id);
    }

    @Override
    public final User add(String id) throws UserAlreadyExistsException {
        Assert.isNotNull(id, () -> new NullPointerException("id is null"));
        return doAdd(id);
    }

    @Override
    public final User addOrGet(String id) {
        Assert.isNotNull(id, () -> new NullPointerException("id is null"));
        return doAddOrGet(id);
    }

    protected abstract User doGet(String id);

    protected abstract User doAdd(String id) throws UserAlreadyExistsException;

    protected abstract User doAddOrGet(String id);

}
