package com.company.energy.util;

import com.company.energy.exception.DAOException;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public final class ConnectionPool implements IConnectionPool {
    private static final Logger logger = Logger.getLogger(ConnectionPool.class);
    private static final ResourceBundle settings = ResourceBundle.getBundle("Settings");
    private static final String URL  = settings.getString("connectionString");
    private static final String USER_NAME = settings.getString("userName");
    private static final String PASSWORD = settings.getString("password");
    private static final int MAX_CONNECTION_NUMBER = Integer.parseInt(settings.getString("maxConnections"));
    private static final List<PooledConnection> availableConnections = Collections.synchronizedList(new ArrayList<>());
    private static IConnectionPool instance;

    private ConnectionPool() throws DAOException {
        init();
    }

    public static IConnectionPool getInstance() throws DAOException {
        return instance;
    }

    public PooledConnection getConnection() throws DAOException {
        if (availableConnections.isEmpty()) {
            String message = "Database connection error!";
            logger.error(message);

            throw new DAOException(message);
        } else {
            return availableConnections.remove(availableConnections.size() - 1);
        }
    }

    public void releaseConnection(PooledConnection connection) throws DAOException {
        if (connection == null) {
            return;
        }

        try {
            if (!connection.getConnection().isClosed()) {
                connection.getConnection().close();
            }

            availableConnections.add(connection);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private static void init() throws DAOException {
        try {
            instance = new ConnectionPool();

            for (int count = 0; count < MAX_CONNECTION_NUMBER; count++) {
                Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                availableConnections.add(new PooledConnection(instance, connection));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }
}
