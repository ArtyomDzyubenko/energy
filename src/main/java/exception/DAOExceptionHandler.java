package exception;

import util.Localization;
import java.sql.SQLException;
import java.util.Enumeration;
import static util.Constants.EMPTY_STRING;

public class DAOExceptionHandler {
    private static DAOExceptionHandler instance;

    private DAOExceptionHandler(){}

    public static synchronized DAOExceptionHandler getInstance(){
        if (instance==null){
            instance = new DAOExceptionHandler();
        }

        return instance;
    }

    public void getExceptionMessage(SQLException e) throws DAOException {
        String exceptionMessage = e.getMessage();
        Enumeration<String> errorKeys = Localization.getDAOErrorLocalization().getKeys();

        String errorMessage = EMPTY_STRING;

        while (errorKeys.hasMoreElements()){
            String key = errorKeys.nextElement();

            if (exceptionMessage.contains(key)){
                errorMessage = Localization.getDAOErrorLocalization().getString(key);
            }
        }

        if (!errorMessage.isEmpty()){
            throw new DAOException(errorMessage);
        } else {
            throw new DAOException(exceptionMessage);
        }
    }
}
