package com.company.energy.service;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.company.energy.util.Constants.REGISTER_USER_JSP;

public class ShowRegisterUserFormService extends AbstractService {
    private static ShowRegisterUserFormService instance;

    private ShowRegisterUserFormService() throws DAOException {}

    public static synchronized ShowRegisterUserFormService getInstance() throws DAOException {
        if (instance == null) {
            instance = new ShowRegisterUserFormService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            request.getRequestDispatcher(REGISTER_USER_JSP).forward(request, response);
        } catch (ServletException | IOException e) {
            throw new ServiceException(e);
        }
    }
}
