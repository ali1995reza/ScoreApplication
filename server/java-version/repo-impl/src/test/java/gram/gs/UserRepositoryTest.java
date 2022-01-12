package gram.gs;

import gram.gs.exceptions.UserAlreadyExistsException;
import gram.gs.model.User;
import org.junit.jupiter.api.Test;

import static gram.gs.TestUtils.newId;
import static org.junit.jupiter.api.Assertions.*;

public interface UserRepositoryTest {

    void testAdd() throws Exception;

    void testGet() throws Exception;

    void testGetOrAdd() throws Exception;

    void testDoesntExistUser() throws Exception;

    void testUserAlreadyException() throws Exception;
}
