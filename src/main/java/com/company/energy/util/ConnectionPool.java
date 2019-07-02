package com.company.energy.util;

import com.company.energy.exception.DAOException;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ConnectionPool {
    private static final Logger logger = Logger.getLogger(ConnectionPool.class);
    private static final ResourceBundle settings = ResourceBundle.getBundle("Settings");
    private static final String URL  = settings.getString("connectionString");
    private static final String USER_NAME = settings.getString("userName");
    private static final String PASSWORD = settings.getString("password");
    private static final int MAX_CONNECTION_NUMBER = Integer.parseInt(settings.getString("maxConnections"));
    private static final List<Connection> availableConnections = new CopyOnWriteArrayList<>();
    private static ConnectionPool instance;

    private ConnectionPool() throws DAOException {
        init();
    }

    public static synchronized ConnectionPool getInstance() throws DAOException {
        if (instance == null) {
            instance = new ConnectionPool();
        }

        return instance;
    }

    public Connection getConnection() throws DAOException {
        if (availableConnections.isEmpty()) {
            String message = "Database connection error!";
            logger.error(message);

            throw new DAOException(message);
        } else {
            return availableConnections.remove(availableConnections.size() - 1);
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            availableConnections.add(connection);
        }
    }

    private static void init() throws DAOException {
        try {
            for (int count = 0; count < MAX_CONNECTION_NUMBER; count++) {
                Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                availableConnections.add(connection);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }
}
