package DAO;

import exception.DAOException;
import exception.DAOExceptionHandler;
import util.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

abstract class AbstractDAO {
    ConnectionPool pool = ConnectionPool.getInstance();
    DAOExceptionHandler exceptionHandler = DAOExceptionHandler.getInstance();

    AbstractDAO() throws DAOException {}

    void deleteEntityById(Long id, String query) throws DAOException {
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            exceptionHandler.getExceptionMessage(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }
}
