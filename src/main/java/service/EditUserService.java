package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.User;
import validator.UserValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static util.Constants.*;
import static util.ServicesAllowedInputParametersLists.editUserServiceAllowedInputParameters;

public class EditUserService extends AbstractService {
    private static EditUserService instance;

    private EditUserService() throws DAOException{}

    public static synchronized EditUserService getInstance() throws DAOException {
        if (instance==null) {
            instance = new EditUserService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, editUserServiceAllowedInputParameters);

            Long userId = getUserId(parameters);
            User user = userDAO.getUserById(userId).get(0);

            request.setAttribute(USER_ATTRIBUTE_NAME, user);
            request.getRequestDispatcher(USERS_JSP).forward(request, response);
        } catch (ServletException e) {
            throw new ServiceException(e);
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }
}
