package service;

import DAO.LanguageDAO;
import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.Language;
import model.User;
import validator.ServiceParametersValidator;
import validator.UserValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static util.Constants.*;

public class AuthService extends AbstractService {
    private static AuthService instance;
    private List<String> allowedParameters = new ArrayList<>();
    private String authUserSessionId = EMPTY_STRING;
    private List<Language> languages = new ArrayList<>();

    private AuthService() throws DAOException {
        init();
    }

    public static synchronized AuthService getInstance() throws DAOException {
        if(instance==null){
            instance = new AuthService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            User authUser = getAuthUser(parameters);
            loadLanguages();

            request.getSession().setAttribute(AUTH_USER, authUser);
            request.getSession().setAttribute(LANGUAGES_ATTRIBUTE_NAME, languages);

            if (authUser.getId() != null) {
                authUserSessionId = request.getSession().getId();
                request.getRequestDispatcher(SHOW_USERS).forward(request, response);
            } else {
                String errorMessage = LanguageService.getInstance().getLocalization().getString("incorrectLoginPassword");
                throw new SecurityException(errorMessage);
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

    public String getAuthUserSessionId() {
        return authUserSessionId;
    }

    private User getAuthUser(Map<String, String[]> parameters) throws ValidationException, DAOException {
        UserValidator userValidator = UserValidator.getInstance();

        String userLoginString = parameters.get(USER_LOGIN)[0];
        String userPasswordString = parameters.get(USER_PASSWORD)[0];
        String userLogin = userValidator.validateLogin(userLoginString, !allowEmpty);
        String userPassword = userValidator.validatePassword(userPasswordString, !allowEmpty);

        return userDAO.getUserByLoginAndPassword(userLogin, userPassword);
    }

    private void loadLanguages() throws DAOException {
        languages = LanguageDAO.getInstance().getAll();
    }

    private void init() {
        allowedParameters.add(USER_LOGIN);
        allowedParameters.add(USER_PASSWORD);
    }
}