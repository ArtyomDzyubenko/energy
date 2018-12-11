package com.epam.energy.exception;

import java.sql.SQLException;

public class DAOException extends Exception {
    public DAOException(SQLException e){
        super(e);
    }
    public DAOException(String message){
        super(message);
    }
}
