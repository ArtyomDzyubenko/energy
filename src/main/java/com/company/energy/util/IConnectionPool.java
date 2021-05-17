package com.company.energy.util;

import com.company.energy.exception.DAOException;

public interface IConnectionPool {
    PooledConnection getConnection() throws DAOException;
    void releaseConnection(PooledConnection connection) throws DAOException;
}
