package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import java.io.IOException;
import com.epam.energy.model.Meter;
import com.epam.energy.model.MeterReader;
import com.epam.energy.model.Resource;
import com.epam.energy.validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.*;
import static com.epam.energy.util.Constants.METERS_JSP;

public class GetMetersService extends AbstractService {
    private static GetMetersService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private GetMetersService() throws DAOException {
        init();
    }

    public static synchronized GetMetersService getInstance() throws DAOException {
        if (instance==null){
            instance = new GetMetersService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long addressId = getAddressId(parameters);
            Long userId = getUserId(parameters);

            String sKey = request.getParameter(SECRET_KEY);
            String authUserSessionId = AuthService.getInstance().getAuthUserSessionId();
            parametersValidator.validateSecretKey(addressId.toString(), authUserSessionId, sKey);

            List<Meter> meters = meterDAO.getMetersByAddressId(addressId);
            List<Resource> resources = resourceDAO.getAll();
            List<MeterReader> readers = meterReaderDAO.getAll();

            saveLastServiceURL(METERS_URL_LAST_STATE, request);
            request.setAttribute(USER_ID, userId);
            request.setAttribute(ADDRESS_ID, addressId);
            request.setAttribute(METERS_ATTRIBUTE_NAME, meters);
            request.setAttribute(RESOURCES_ATTRIBUTE_NAME, resources);
            request.setAttribute(METER_READERS_ATTRIBUTE_NAME, readers);
            request.getRequestDispatcher(METERS_JSP).forward(request, response);
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

    private void init() {
        allowedParameters.add(ADDRESS_ID);
        allowedParameters.add(USER_ID);
        allowedParameters.add(SECRET_KEY);
    }
}
