package com.company.energy.service;

import com.company.energy.dao.AbstractUserDAO;
import com.company.energy.dao.UserDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.dao.LanguageDAO;

import java.io.IOException;
import com.company.energy.model.Language;
import com.company.energy.model.User;
import com.company.energy.validator.ServiceParametersValidator;
import com.company.energy.validator.UserValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class AuthService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractUserDAO userDAO = UserDAO.getInstance();

    private static AuthService instance;

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

            List<Language> languages = loadLanguages();
            request.getSession().setAttribute(AUTHORIZED_USER, authUser);
            request.getSession().setAttribute(LANGUAGES_ATTRIBUTE, languages);

            if (authUser.getId() != null) {
                request.getRequestDispatcher(SHOW_USERS).forward(request, response);
            } else {
                String errorMessage = LanguageService.getInstance().getLocalization().getString("incorrectLoginPassword");
                throw new SecurityException(errorMessage);
            }
        } catch (ServletException | IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private User getAuthUser(Map<String, String[]> parameters) throws ValidationException, DAOException {
        UserValidator userValidator = UserValidator.getInstance();

        String userLogin = userValidator.validateAndGetLogin(parameters.get(USER_LOGIN)[0], !allowEmpty);
        String userPassword = userValidator.validateAndGetPassword(parameters.get(USER_PASSWORD)[0], !allowEmpty);

        return userDAO.getUserByLoginAndPassword(userLogin, userPassword);
    }

    private List<Language> loadLanguages() throws DAOException {
        return LanguageDAO.getInstance().getAll();
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(USER_LOGIN, USER_PASSWORD));
    }
}