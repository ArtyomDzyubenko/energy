package com.company.energy.service;

import com.company.energy.dao.*;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;

import java.io.IOException;
import com.company.energy.model.Meter;
import com.company.energy.model.MeterReader;
import com.company.energy.model.Resource;
import com.company.energy.validator.AddressValidator;
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
import static com.company.energy.util.Constants.METERS_JSP;

public class GetMetersService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractMeterDAO meterDAO = MeterDAO.getInstance();
    private static final AbstractMeterReaderDAO meterReaderDAO = MeterReaderDAO.getInstance();
    private static final AbstractResourceDAO resourceDAO = ResourceDAO.getInstance();
    private static final AddressValidator addressValidator = AddressValidator.getInstance();
    private static final UserValidator userValidator = UserValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static GetMetersService instance;

    private GetMetersService() throws DAOException {
        init();
    }

    public static synchronized GetMetersService getInstance() throws DAOException {
        if (instance == null) {
            instance = new GetMetersService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();

            parametersValidator.validate(parameters, allowedParameters);

            Long addressId = addressValidator.validateAndGetId(parameters.get(ADDRESS_ID)[0], allowEmpty);
            Long userId = userValidator.validateAndGetId(parameters.get(USER_ID)[0], !allowEmpty);
            String sKey = request.getParameter(SECRET_KEY);
            String authorizedUserSessionId = AuthService.getInstance().getAuthorizedUserSessionId();

            parametersValidator.validateSecretKey(addressId.toString(), authorizedUserSessionId, sKey);

            List<Meter> meters = meterDAO.getMetersByAddressId(addressId);
            List<Resource> resources = resourceDAO.getAll();
            List<MeterReader> readers = meterReaderDAO.getAll();

            saveLastServiceURL(METERS_URL_LAST_STATE, request);
            request.setAttribute(USER_ID, userId);
            request.setAttribute(ADDRESS_ID, addressId);
            request.setAttribute(METERS_ATTRIBUTE, meters);
            request.setAttribute(RESOURCES_ATTRIBUTE, resources);
            request.setAttribute(METER_READERS_ATTRIBUTE, readers);
            request.getRequestDispatcher(METERS_JSP).forward(request, response);
        } catch (ServletException | IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(ADDRESS_ID, USER_ID, SECRET_KEY));
    }
}
