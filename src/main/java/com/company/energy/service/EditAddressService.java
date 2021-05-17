package com.company.energy.service;

import com.company.energy.dao.*;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.model.Address;
import com.company.energy.model.Street;
import com.company.energy.model.User;
import com.company.energy.validator.AddressValidator;
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
import static com.company.energy.util.Constants.ADDRESSES_JSP;

public class EditAddressService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractAddressDAO addressDAO = AddressDAO.getInstance();
    private static final AbstractStreetDAO streetDAO = StreetDAO.getInstance();
    private static final AbstractUserDAO userDAO = UserDAO.getInstance();
    private static final AddressValidator addressValidator = AddressValidator.getInstance();
    private static final UserValidator userValidator = UserValidator.getInstance();

    private static EditAddressService instance;

    private EditAddressService() throws DAOException {
        init();
    }

    public static synchronized EditAddressService getInstance() throws DAOException {
        if (instance == null) {
            instance = new EditAddressService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long userId = userValidator.validateAndGetId(parameters.get(USER_ID)[0], !allowEmpty);
            Long addressId = addressValidator.validateAndGetId(parameters.get(ADDRESS_ID)[0], !allowEmpty);

            Address address = addressDAO.getAddressById(addressId).get(0);
            List<Street> streets = streetDAO.getAll();
            List<User> users = userDAO.getAll();

            request.setAttribute(USER_ID, userId);
            request.setAttribute(ADDRESS_ATTRIBUTE, address);
            request.setAttribute(STREETS_ATTRIBUTE, streets);
            request.setAttribute(USERS_ATTRIBUTE, users);
            request.getRequestDispatcher(ADDRESSES_JSP).forward(request, response);
        } catch (ServletException | IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(ADDRESS_ID, USER_ID));
    }
}
