package com.company.energy.service;

import com.company.energy.dao.AbstractUserDAO;
import com.company.energy.dao.UserDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;

import java.io.IOException;
import java.util.List;
import com.company.energy.model.User;
import com.company.energy.util.Encryption;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.company.energy.util.Constants.*;

public class GetUsersService extends AbstractService {
    private static final AbstractUserDAO userDAO = UserDAO.getInstance();

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
            } else {
                users = userDAO.getUserById(authUser.getId());
            }

            users.forEach(user -> user.setSecretKey(Encryption.encrypt(user.getId() + request.getSession().getId())));

            request.setAttribute(USERS_ATTRIBUTE, users);

            saveLastServiceURL(USERS_URL_LAST_STATE, request);
            request.getRequestDispatcher(USERS_JSP).forward(request, response);
        } catch (ServletException | IOException | DAOException e) {
            throw new ServiceException(e);
        }
    }
}
