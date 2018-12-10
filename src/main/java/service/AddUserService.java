package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.User;
import validator.UserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static util.Constants.*;
import static util.ServicesAllowedInputParametersLists.addUserServiceAllowedInputParameters;

public class AddUserService extends AbstractService {
    private static AddUserService instance;

    private AddUserService() throws DAOException{}

    public static AddUserService getInstance() throws DAOException {
        if (instance==null) {
            instance = new AddUserService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, addUserServiceAllowedInputParameters);

            User user = getUser(parameters);

            if(user.getId().equals(LONG_ZERO)) {
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
}
