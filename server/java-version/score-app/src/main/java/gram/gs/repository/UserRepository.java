package gram.gs.repository;

import gram.gs.exceptions.UserAlreadyExistsException;
import gram.gs.model.User;

public interface UserRepository {

    /**
     * Return the user if exists
     *
     * @param id user identifier
     * @return a {@link User} instance if exists and null if not
     */
    User get(String id);

    /**
     * Add a new user to the repository
     *
     * @param id user identifier
     * @return a {@link User} instance which added to repository
     * @throws UserAlreadyExistsException When user already exists
     */
    User add(String id) throws UserAlreadyExistsException;

    /**
     * Like {@link UserRepository#add(String)} but if player already exists throw nothing and return exists one
     *
     * @param id user identifier
     * @return a {@link User} instance which never null
     */
    User addOrGet(String id);

    /**
     * Clear whole repository
     */
    void clear();

}
