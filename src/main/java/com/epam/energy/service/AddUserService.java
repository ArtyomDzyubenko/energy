package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import com.epam.energy.model.User;
import com.epam.energy.validator.ServiceParametersValidator;
import com.epam.energy.validator.UserValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.*;

public class AddUserService extends AbstractService {
    private static AddUserService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private AddUserService() throws DAOException{
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
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            User user = getUser(parameters);

            if (user.getId().equals(LONG_ZERO)) {
                userDAO.addUser(user);
            } else {
                userDAO.editUser(user);
            }

            response.sendRedirect(getLastServiceURL(USERS_URL_LAST_STATE, request));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private User getUser(Map<String, String[]> parameters) throws ValidationException, DAOException {
        UserValidator userValidator = UserValidator.getInstance();

        Long userId = getUserId(parameters, allowEmpty);
        String userLogin = getUserLogin(parameters);
        String userPassword = getUserPassword(parameters);
        String userFirstName = userValidator.validateFirstName(parameters.get(USER_FIRST_NAME)[0], !allowEmpty);
        String userLastName = userValidator.validateLastName(parameters.get(USER_LAST_NAME)[0], allowEmpty);
        Long userPhone = userValidator.validatePhone(parameters.get(USER_PHONE)[0], !allowEmpty);
        String userEmail = userValidator.validateEmail(parameters.get(USER_EMAIL)[0], allowEmpty);
        Integer userPersonalAccount = userValidator.validatePersonalAccount(parameters.get(USER_PERSONAL_ACCOUNT)[0], allowEmpty);

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
