package com.company.energy.service;

import com.company.energy.dao.AbstractUserDAO;
import com.company.energy.dao.UserDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;

import java.io.IOException;
import com.company.energy.model.User;
import com.company.energy.util.Encryption;
import com.company.energy.validator.ServiceParametersValidator;
import com.company.energy.validator.UserValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class EditUserService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractUserDAO userDAO = UserDAO.getInstance();
    private static final UserValidator userValidator = UserValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static EditUserService instance;

    private EditUserService() throws DAOException {
        init();
    }

    public static synchronized EditUserService getInstance() throws DAOException {
        if (instance == null) {
            instance = new EditUserService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long userId = userValidator.validateAndGetId(parameters.get(USER_ID)[0], !allowEmpty);
            User user = userDAO.getUserById(userId).get(0);
            user.setSecretKey(Encryption.encrypt(user.getId() + request.getSession().getId()));

            request.setAttribute(USER_ATTRIBUTE, user);
            request.getRequestDispatcher(USERS_JSP).forward(request, response);
        } catch (ServletException | IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.add(USER_ID);
    }
}
