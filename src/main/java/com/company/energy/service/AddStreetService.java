package com.company.energy.service;

import com.company.energy.dao.AbstractStreetDAO;
import com.company.energy.dao.StreetDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.model.Street;
import com.company.energy.validator.ServiceParametersValidator;
import com.company.energy.validator.StreetValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class AddStreetService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractStreetDAO streetDAO = StreetDAO.getInstance();
    private static final StreetValidator streetValidator = StreetValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static AddStreetService instance;

    private AddStreetService() throws DAOException {
        init();
    }

    public static synchronized AddStreetService getInstance() throws DAOException {
        if (instance == null) {
            instance = new AddStreetService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Street street = getStreet(parameters);

            if (street.getId().equals(LONG_ZERO)) {
                streetDAO.addStreet(street);
            } else {
                streetDAO.editStreet(street);
            }

            response.sendRedirect(getLastServiceURL(STREETS_URL_LAST_STATE, request));
        } catch (IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private Street getStreet(Map<String, String[]> parameters) throws ValidationException, DAOException {
        Long streetId = streetValidator.validateAndGetId(parameters.get(STREET_ID)[0], allowEmpty);
        String streetName = streetValidator.validateAndGetName(parameters.get(STREET_NAME)[0], !allowEmpty);

        Street street = new Street();
        street.setId(streetId);
        street.setName(streetName);

        return street;
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(STREET_ID, STREET_NAME));
    }
}
