package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import model.User;
import validator.UserValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import static util.Constants.*;
import static util.ServicesAllowedInputParametersLists.registerUserServiceAllowedInputParameters;

public class RegisterUserService extends AbstractService {
    private static RegisterUserService instance;

    private RegisterUserService() throws DAOException{}

    public static synchronized RegisterUserService getInstance() throws DAOException {
        if (instance==null) {
            instance = new RegisterUserService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, registerUserServiceAllowedInputParameters);

            User registeredUser = getRegisteredUser(parameters);

            if (registeredUser.getId().equals(LONG_ZERO)) {
                userDAO.registerUser(registeredUser);
            } else {
                userDAO.updateRegisteredUser(registeredUser);
            }

            User user = userDAO.getUserByLoginAndPassword(registeredUser.getLogin(), registeredUser.getPassword());

            if (user != null) {
                request.setAttribute(USER_LOGIN, registeredUser.getLogin());
                request.setAttribute(USER_PASSWORD, registeredUser.getPassword());
                request.getRequestDispatcher(AUTH).forward(request, response);
            } else {
                request.getRequestDispatcher(INDEX_JSP).forward(request, response);
            }
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

    private User getRegisteredUser(Map<String, String[]> parameters) throws ValidationException, DAOException {
        UserValidator userValidator = UserValidator.getInstance();

        String userLoginString = parameters.get(USER_LOGIN)[0];
        String userPasswordString = parameters.get(USER_PASSWORD)[0];
        String userPhoneString = parameters.get(USER_PHONE)[0];
        String userEmailString = parameters.get(USER_EMAIL)[0];

        Long userId = getUserId(parameters);
        String userLogin = userValidator.validateLogin(userLoginString, !allowEmpty);
        String userPassword = userValidator.validatePassword(userPasswordString, !allowEmpty);
        Long userPhone = userValidator.validatePhone(userPhoneString, allowEmpty);
        String userEmail = userValidator.validateEmail(userEmailString, allowEmpty);

        User user = new User();
        user.setId(userId);
        user.setLogin(userLogin);
        user.setPassword(userPassword);
        user.setPhone(userPhone);
        user.setEmail(userEmail);

        return user;
    }
}
