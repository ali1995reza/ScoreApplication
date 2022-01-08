package gram.gs.repository.impl.sql;

import gram.gs.repository.impl.ValidatedScoreRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SqlScoreRepositoryAbstract extends ValidatedScoreRepository {

    private final DataSource dataSource;

    public SqlScoreRepositoryAbstract(DataSource dataSource) {
        this.dataSource = dataSource;
        init();
    }

    protected DataSource getDataSource() {
        return dataSource;
    }

    protected Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    protected void execute(String sql) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    protected ResultSet executeQuery(String sql) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    protected void truncate(String table) {
        execute("TRUNCATE TABLE " + table);
    }

    protected abstract void init();
}
