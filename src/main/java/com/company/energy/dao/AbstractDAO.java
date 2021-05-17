package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.DAOExceptionHandler;
import com.company.energy.util.ConnectionPool;
import com.company.energy.util.IConnectionPool;
import com.company.energy.util.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

abstract class AbstractDAO {
    IConnectionPool pool = ConnectionPool.getInstance();
    DAOExceptionHandler exceptionHandler = DAOExceptionHandler.getInstance();

    AbstractDAO() throws DAOException {}

    void deleteEntityById(Long id, String query) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.getExceptionMessage(e);
        }
    }
}
