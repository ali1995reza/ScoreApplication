package gram.gs.repository.impl.sql;

import gram.gs.assertion.Assert;
import gram.gs.exceptions.UserAlreadyExistsException;
import gram.gs.model.User;

import javax.sql.DataSource;
import java.sql.*;

public class SqlUserRepository extends SqlUserRepositoryAbstract {

    private final String tableName = "users";
    private final String getSQL = "SELECT * FROM " + tableName + " WHERE id=?";
    private final String insertSQL = "INSERT INTO " + tableName + " VALUES (?)";

    public SqlUserRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected User doGet(String id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(getSQL)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getString(1));
            }
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected User doAdd(String id) throws UserAlreadyExistsException {
        Assert.isTrue(insert(id), UserAlreadyExistsException::new);
        return new User(id);
    }

    @Override
    protected User doAddOrGet(String id) {
        insert(id);
        return new User(id);
    }

    @Override
    protected void init() {
        execute("CREATE TABLE IF NOT EXISTS " + tableName + "(id VARCHAR(50) PRIMARY KEY)");
    }

    @Override
    public void clear() {
        truncate(tableName);
    }

    private boolean insert(String id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, id);
            if (statement.executeUpdate() < 0) {
                throw new IllegalStateException("can not add user right now");
            }
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
