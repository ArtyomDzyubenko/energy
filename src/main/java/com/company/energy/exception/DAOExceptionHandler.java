package com.company.energy.exception;

import com.company.energy.service.LanguageService;
import com.company.energy.util.Constants;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class DAOExceptionHandler {
    private static DAOExceptionHandler instance;

    private DAOExceptionHandler() {}

    public static synchronized DAOExceptionHandler getInstance() {
        if (instance == null) {
            instance = new DAOExceptionHandler();
        }

        return instance;
    }

    public void getExceptionMessage(SQLException e) throws DAOException {
        String exceptionMessage = e.getMessage();
        ResourceBundle localization = LanguageService.getInstance().getDAOErrorLocalization();
        Enumeration<String> errorKeys = localization.getKeys();

        String errorMessage = Constants.EMPTY_STRING;

        while (errorKeys.hasMoreElements()) {
            String key = errorKeys.nextElement();

            if (exceptionMessage.contains(key)) {
                errorMessage = localization.getString(key);
            }
        }

        if (!errorMessage.isEmpty()) {
            throw new DAOException(errorMessage);
        } else {
            throw new DAOException(e);
        }
    }
}