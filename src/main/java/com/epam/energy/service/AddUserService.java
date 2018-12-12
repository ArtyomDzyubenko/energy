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

        String userLoginString = parameters.get(USER_LOGIN)[0];
        String userPasswordString = parameters.get(USER_PASSWORD)[0];
        String userFirstNameString = parameters.get(USER_FIRST_NAME)[0];
        String userLastNameString = parameters.get(USER_LAST_NAME)[0];
        String userPhoneString = parameters.get(USER_PHONE)[0];
        String userEmailString = parameters.get(USER_EMAIL)[0];
        String userPersonalAccountString = parameters.get(USER_PERSONAL_ACCOUNT)[0];

        Long userId = getUserId(parameters);
        String userLogin = userValidator.validateLogin(userLoginString, !allowEmpty);
        String userPassword = userValidator.validatePassword(userPasswordString, !allowEmpty);
        String userFirstName = userValidator.validateFirstName(userFirstNameString, !allowEmpty);
        String userLastName = userValidator.validateLastName(userLastNameString, allowEmpty);
        Long userPhone = userValidator.validatePhone(userPhoneString, !allowEmpty);
        String userEmail = userValidator.validateEmail(userEmailString, allowEmpty);
        Integer userPersonalAccount = userValidator.validatePersonalAccount(userPersonalAccountString, allowEmpty);

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
        allowedParameters.add(USER_ID);
        allowedParameters.add(USER_LOGIN);
        allowedParameters.add(USER_PASSWORD);
        allowedParameters.add(USER_FIRST_NAME);
        allowedParameters.add(USER_LAST_NAME);
        allowedParameters.add(USER_PHONE);
        allowedParameters.add(USER_EMAIL);
        allowedParameters.add(USER_PERSONAL_ACCOUNT);
    }
}
