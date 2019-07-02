package com.company.energy.service;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;

import java.io.IOException;
import java.util.List;
import com.company.energy.model.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.company.energy.util.Constants.*;

public class GetUsersService extends AbstractService {
    private static GetUsersService instance;

    private GetUsersService() throws DAOException {}

    public static synchronized GetUsersService getInstance() throws DAOException {
        if (instance == null) {
            instance = new GetUsersService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            User authUser = (User) request.getSession().getAttribute(AUTHORIZED_USER);
            List<User> users;

            if (authUser.isAdmin()) {
                users = userDAO.getAll();
                request.setAttribute(USERS_ATTRIBUTE, users);
            } else {
                users = userDAO.getUserById(authUser.getId());
                request.setAttribute(USERS_ATTRIBUTE, users);
            }

            saveLastServiceURL(USERS_URL_LAST_STATE, request);
            request.getRequestDispatcher(USERS_JSP).forward(request, response);
        } catch (ServletException e) {
            throw new ServiceException(e);
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
