package com.company.energy.service;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static com.company.energy.util.Constants.INDEX_JSP;

public class LogOutService extends AbstractService {
    private static LogOutService instance;

    private LogOutService() throws DAOException {}

    public static synchronized LogOutService getInstance() throws DAOException {
        if (instance == null) {
            instance = new LogOutService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            request.getSession().invalidate();
            request.getRequestDispatcher(INDEX_JSP).forward(request, response);
        } catch (ServletException | IOException e) {
            throw new ServiceException(e);
        }
    }
}
