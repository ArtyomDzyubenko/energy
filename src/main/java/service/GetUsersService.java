package service;

import exception.DAOException;
import exception.ServiceException;
import java.io.IOException;
import java.util.List;
import model.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static util.Constants.*;

public class GetUsersService extends AbstractService {
    private static GetUsersService instance;

    private GetUsersService() throws DAOException {}

    public static synchronized GetUsersService getInstance() throws DAOException {
        if (instance==null) {
            instance = new GetUsersService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            User authUser = (User) request.getSession().getAttribute(AUTH_USER);
            List<User> users;

            if (authUser.isAdmin()) {
                users = userDAO.getAll();
                request.setAttribute(USERS_ATTRIBUTE_NAME, users);
            } else {
                users = userDAO.getUserById(authUser.getId());
                request.setAttribute(USERS_ATTRIBUTE_NAME, users);
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
