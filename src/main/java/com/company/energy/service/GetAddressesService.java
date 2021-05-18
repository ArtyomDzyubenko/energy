package com.company.energy.service;

import com.company.energy.dao.AbstractAddressDAO;
import com.company.energy.dao.AbstractStreetDAO;
import com.company.energy.dao.AddressDAO;
import com.company.energy.dao.StreetDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;

import java.io.IOException;
import com.company.energy.model.Address;
import com.company.energy.model.Street;
import com.company.energy.util.Encryption;
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

public class GetAddressesService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractAddressDAO addressDAO = AddressDAO.getInstance();
    private static final AbstractStreetDAO streetDAO = StreetDAO.getInstance();
    private static final UserValidator userValidator = UserValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static GetAddressesService instance;

    private GetAddressesService() throws DAOException {
        init();
    }

    public static synchronized GetAddressesService getInstance() throws DAOException {
        if (instance == null) {
            instance = new GetAddressesService();
        }

        return  instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();

            parametersValidator.validate(parameters, allowedParameters);

            Long userId = userValidator.validateAndGetId(parameters.get(USER_ID)[0], !allowEmpty);

            parametersValidator.validateSecretKey(userId.toString(), request.getSession().getId(), parameters.get(SECRET_KEY)[0]);

            List<Address> address = addressDAO.getAddressesByUserId(userId);
            address.forEach(a -> a.setSecretKey(Encryption.encrypt(a.getId() + request.getSession().getId())));

            List<Street> streets = streetDAO.getAll();

            saveLastServiceURL(ADDRESSES_URL_LAST_STATE, request);
            request.setAttribute(USER_ID, userId);
            request.setAttribute(ADDRESSES_ATTRIBUTE, address);
            request.setAttribute(STREETS_ATTRIBUTE, streets);
            request.getRequestDispatcher(ADDRESSES_JSP).forward(request, response);
        } catch (ServletException | IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(USER_ID, SECRET_KEY));
    }
}
