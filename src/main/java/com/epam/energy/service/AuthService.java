package com.epam.energy.service;

import com.epam.energy.dao.LanguageDAO;
import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import java.io.IOException;
import com.epam.energy.model.Language;
import com.epam.energy.model.User;
import com.epam.energy.validator.ServiceParametersValidator;
import com.epam.energy.validator.UserValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.*;

public class AuthService extends AbstractService {
    private static AuthService instance;
    private List<String> allowedParameters = new ArrayList<>();
    private String authUserSessionId = EMPTY_STRING;
    private List<Language> languages = new ArrayList<>();

    private AuthService() throws DAOException {
        init();
    }

    public static synchronized AuthService getInstance() throws DAOException {
        if (instance == null) {
            instance = new AuthService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            User authUser = getAuthUser(parameters);
            loadLanguages();

            request.getSession().setAttribute(AUTHORIZED_USER, authUser);
            request.getSession().setAttribute(LANGUAGES_ATTRIBUTE, languages);

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

        String userLogin = userValidator.validateLogin(parameters.get(USER_LOGIN)[0], !allowEmpty);
        String userPassword = userValidator.validatePassword(parameters.get(USER_PASSWORD)[0], !allowEmpty);

        return userDAO.getUserByLoginAndPassword(userLogin, userPassword);
    }

    private void loadLanguages() throws DAOException {
        languages = LanguageDAO.getInstance().getAll();
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(USER_LOGIN, USER_PASSWORD));
    }
}