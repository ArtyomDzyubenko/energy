package com.company.energy.service;

import com.company.energy.dao.AbstractMeasurementDAO;
import com.company.energy.dao.MeasurementDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;

import java.io.IOException;
import com.company.energy.model.Measurement;
import com.company.energy.util.Encryption;
import com.company.energy.validator.AddressValidator;
import com.company.energy.validator.MeterValidator;
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

public class GetMeasurementsService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractMeasurementDAO measurementDAO = MeasurementDAO.getInstance();
    private static final AddressValidator addressValidator = AddressValidator.getInstance();
    private static final MeterValidator meterValidator = MeterValidator.getInstance();
    private static final UserValidator userValidator = UserValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();


    private static GetMeasurementsService instance;

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
            Map<String, String[]> parameters = request.getParameterMap();

            parametersValidator.validate(parameters, allowedParameters);

            Long meterId = meterValidator.validateAndGetId(parameters.get(METER_ID)[0], !allowEmpty);

            parametersValidator.validateSecretKey(meterId.toString(), request.getSession().getId(), parameters.get(SECRET_KEY)[0]);

            Long addressId = addressValidator.validateAndGetId(parameters.get(ADDRESS_ID)[0], !allowEmpty);
            Long userId = userValidator.validateAndGetId(parameters.get(USER_ID)[0], !allowEmpty);
            List<Measurement> measurements = measurementDAO.getMeasurementsByMeterId(meterId);
            measurements.forEach(measurement -> measurement.setSecretKey(Encryption.encrypt(measurement.getId() + request.getSession().getId())));

            saveLastServiceURL(MEASUREMENTS_URL_LAST_STATE, request);
            request.setAttribute(METER_ID, meterId);
            request.setAttribute(ADDRESS_ID, addressId);
            request.setAttribute(USER_ID, userId);
            request.setAttribute(MEASUREMENTS_ATTRIBUTE, measurements);
            request.getRequestDispatcher(MEASUREMENTS_JSP).forward(request, response);
        } catch (ServletException | IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(METER_ID, ADDRESS_ID, USER_ID, SECRET_KEY));
    }
}
