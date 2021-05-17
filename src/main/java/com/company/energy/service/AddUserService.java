package com.company.energy.service;

import com.company.energy.dao.AbstractUserDAO;
import com.company.energy.dao.UserDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.model.User;
import com.company.energy.validator.ServiceParametersValidator;
import com.company.energy.validator.UserValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class AddUserService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractUserDAO userDAO = UserDAO.getInstance();
    private static final UserValidator userValidator = UserValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static AddUserService instance;

    private AddUserService() throws DAOException {
        init();
    }

    public static AddUserService getInstance() throws DAOException {
        if (instance == null) {
            instance = new AddUserService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            User user = getUser(parameters);

            if (user.getId().equals(LONG_ZERO)) {
                userDAO.addUser(user);
            } else {
                userDAO.editUser(user);
            }

            response.sendRedirect(getLastServiceURL(USERS_URL_LAST_STATE, request));
        } catch (IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private User getUser(Map<String, String[]> parameters) throws ValidationException, DAOException {
        Long userId = userValidator.validateAndGetId(parameters.get(USER_ID)[0], allowEmpty);
        String userLogin = userValidator.validateAndGetLogin(parameters.get(USER_LOGIN)[0], !allowEmpty);
        String userPassword = userValidator.validateAndGetPassword(parameters.get(USER_PASSWORD)[0], !allowEmpty);
        String userFirstName = userValidator.validateAndGetFirstName(parameters.get(USER_FIRST_NAME)[0], !allowEmpty);
        String userLastName = userValidator.validateAndGetLastName(parameters.get(USER_LAST_NAME)[0], allowEmpty);
        Long userPhone = userValidator.validateAndGetPhone(parameters.get(USER_PHONE)[0], !allowEmpty);
        String userEmail = userValidator.validateAndGetEmail(parameters.get(USER_EMAIL)[0], allowEmpty);
        Integer userPersonalAccount = userValidator.validateAndGetPersonalAccount(parameters.get(USER_PERSONAL_ACCOUNT)[0], allowEmpty);

        User user = new User();
        user.setId(userId);
        user.setLogin(userLogin);
        user.setPassword(userPassword);
        user.setFirstName(userFirstName);
        user.setLastName(userLastName);
        user.setPhone(userPhone);
        user.setEmail(userEmail);
        user.setPersonalAccount(userPersonalAccount);

        return user;
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(USER_ID, USER_LOGIN, USER_PASSWORD, USER_FIRST_NAME, USER_LAST_NAME,
                USER_PHONE, USER_EMAIL, USER_PERSONAL_ACCOUNT));
    }
}
