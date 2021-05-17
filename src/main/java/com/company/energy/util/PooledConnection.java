package com.company.energy.util;

import com.company.energy.exception.DAOException;

import java.sql.Connection;

public class PooledConnection implements AutoCloseable {
    private IConnectionPool pool;
    private Connection connection;

    public PooledConnection(IConnectionPool pool, Connection connection) {
        this.pool = pool;
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws DAOException {
        pool.releaseConnection(this);
    }
}
