package gram.gs.repository;

import gram.gs.model.User;

public interface UserRepository {

    User get(String id);

    User add(String id);

    User addOrGet(String id);

    void clear();

}
