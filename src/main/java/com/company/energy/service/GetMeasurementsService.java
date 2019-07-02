package com.company.energy.service;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;

import java.io.IOException;
import com.company.energy.model.Measurement;
import com.company.energy.validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class GetMeasurementsService extends AbstractService {
    private static GetMeasurementsService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private GetMeasurementsService() throws DAOException {
        init();
    }

    public static synchronized GetMeasurementsService getInstance() throws DAOException {
        if (instance == null) {
            instance = new GetMeasurementsService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();

            parametersValidator.validate(parameters, allowedParameters);

            Long meterId = getMeterId(parameters, !allowEmpty);
            String secretKey = parameters.get(SECRET_KEY)[0];
            String authorizedUserSessionId = AuthService.getInstance().getAuthorizedUserSessionId();

            parametersValidator.validateSecretKey(meterId.toString(), authorizedUserSessionId, secretKey);

            Long addressId = getAddressId(parameters, !allowEmpty);
            Long userId = getUserId(parameters, !allowEmpty);
            List<Measurement> measurements = measurementDAO.getMeasurementsByMeterId(meterId);

            saveLastServiceURL(MEASUREMENTS_URL_LAST_STATE, request);
            request.setAttribute(METER_ID, meterId);
            request.setAttribute(ADDRESS_ID, addressId);
            request.setAttribute(USER_ID, userId);
            request.setAttribute(MEASUREMENTS_ATTRIBUTE, measurements);
            request.getRequestDispatcher(MEASUREMENTS_JSP).forward(request, response);
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
        allowedParameters.addAll(Arrays.asList(METER_ID, ADDRESS_ID, USER_ID, SECRET_KEY));
    }
}
