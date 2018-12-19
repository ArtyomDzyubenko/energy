package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import com.epam.energy.model.User;
import com.epam.energy.validator.ServiceParametersValidator;
import com.epam.energy.validator.UserValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.*;

public class RegisterUserService extends AbstractService {
    private static RegisterUserService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private RegisterUserService() throws DAOException{
        init();
    }

    public static synchronized RegisterUserService getInstance() throws DAOException {
        if (instance == null) {
            instance = new RegisterUserService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

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

        Long userId = getUserId(parameters);
        String userLogin = getUserLogin(parameters);
        String userPassword = getUserPassword(parameters);
        Long userPhone = userValidator.validatePhone(parameters.get(USER_PHONE)[0], allowEmpty);
        String userEmail = userValidator.validateEmail(parameters.get(USER_EMAIL)[0], allowEmpty);

        User user = new User();
        user.setId(userId);
        user.setLogin(userLogin);
        user.setPassword(userPassword);
        user.setPhone(userPhone);
        user.setEmail(userEmail);
        user.setAdmin(false);

        return user;
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(USER_ID, USER_LOGIN, USER_PASSWORD, USER_PHONE, USER_EMAIL));
    }
}
