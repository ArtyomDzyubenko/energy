package com.company.energy.service;

import com.company.energy.dao.AbstractUserDAO;
import com.company.energy.dao.UserDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.model.User;
import com.company.energy.validator.ServiceParametersValidator;
import com.company.energy.validator.UserValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class RegisterUserService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractUserDAO userDAO = UserDAO.getInstance();
    private static final UserValidator userValidator = UserValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static RegisterUserService instance;

    private RegisterUserService() throws DAOException {
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
            Map<String, String[]> parameters = request.getParameterMap();

            parametersValidator.validate(parameters, allowedParameters);

            User registeredUser = getRegisteredUser(parameters);

            if (registeredUser.getId().equals(LONG_ZERO)) {
                userDAO.registerUser(registeredUser);
            } else {
                userDAO.updateRegisteredUser(registeredUser);
            }

            User user = userDAO.getUserByLoginAndPassword(registeredUser.getLogin(), registeredUser.getPassword());

            if (!user.getId().equals(LONG_ZERO)) {
                request.setAttribute(USER_LOGIN, registeredUser.getLogin());
                request.setAttribute(USER_PASSWORD, registeredUser.getPassword());
                request.getRequestDispatcher(AUTH).forward(request, response);
            } else {
                request.getRequestDispatcher(INDEX_JSP).forward(request, response);
            }
        } catch (ServletException | IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private User getRegisteredUser(Map<String, String[]> parameters) throws ValidationException, DAOException {
        Long userId = userValidator.validateAndGetId(parameters.get(USER_ID)[0], allowEmpty);
        String userLogin = userValidator.validateAndGetLogin(parameters.get(USER_LOGIN)[0], !allowEmpty);
        String userPassword = userValidator.validateAndGetPassword(parameters.get(USER_LOGIN)[0], !allowEmpty);
        Long userPhone = userValidator.validateAndGetPhone(parameters.get(USER_PHONE)[0], allowEmpty);
        String userEmail = userValidator.validateAndGetEmail(parameters.get(USER_EMAIL)[0], allowEmpty);

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
