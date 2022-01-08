package gram.gs.repository;

import gram.gs.exceptions.UserAlreadyExistsException;
import gram.gs.model.User;

public interface UserRepository {

    User get(String id);

    User add(String id) throws UserAlreadyExistsException;

    User addOrGet(String id);

    void clear();

}
