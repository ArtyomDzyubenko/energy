package com.company.energy.service;

import com.company.energy.dao.AbstractMeasurementDAO;
import com.company.energy.dao.MeasurementDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;

import java.io.IOException;
import com.company.energy.model.Measurement;
import com.company.energy.validator.MeasurementValidator;
import com.company.energy.validator.MeterValidator;
import com.company.energy.validator.ServiceParametersValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class AddMeasurementService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractMeasurementDAO measurementDAO = MeasurementDAO.getInstance();
    private static final MeasurementValidator measurementValidator = MeasurementValidator.getInstance();
    private static final MeterValidator meterValidator = MeterValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static AddMeasurementService instance;

    private AddMeasurementService() throws DAOException {
        init();
    }

    public static synchronized AddMeasurementService getInstance() throws DAOException {
        if (instance == null) {
            instance = new AddMeasurementService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Measurement measurement = getMeasurement(parameters);

            if (measurement.getId().equals(LONG_ZERO)) {
                measurementDAO.addMeasurement(measurement);
            } else {
                measurementDAO.editMeasurement(measurement);
            }

            response.sendRedirect(getLastServiceURL(MEASUREMENTS_URL_LAST_STATE, request));
        } catch (IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private Measurement getMeasurement(Map<String, String[]> parameters) throws ValidationException, DAOException {
        String dateTimeString = EMPTY_STRING;

        Long  measurementId = measurementValidator.validateAndGetId(parameters.get(MEASUREMENT_ID)[0], allowEmpty);
        Double measurementValue = measurementValidator.validateAndGetValue(parameters.get(MEASUREMENT_VALUE)[0], !allowEmpty);
        Long meterId = meterValidator.validateAndGetId(parameters.get(METER_ID)[0], allowEmpty);
        Timestamp dateTime;

        Measurement measurement = new Measurement();
        measurement.setId(measurementId);

        if (parameters.containsKey(MEASUREMENT_LOCAL_DATE_TIME_STRING)) {
            dateTimeString = parameters.get(MEASUREMENT_LOCAL_DATE_TIME_STRING)[0];
        } else if (parameters.containsKey(MEASUREMENT_DATE_TIME)) {
            dateTimeString = parameters.get(MEASUREMENT_DATE_TIME)[0];
        }

        dateTimeString = dateTimeString.replace(DATE_TIME_DELIMITER, SPACE);
        dateTime = measurementValidator.validateAndGetDate(dateTimeString, !allowEmpty);

        measurement.setDateTime(dateTime);
        measurement.setValue(measurementValue);
        measurement.setMeterId(meterId);

        return measurement;
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(MEASUREMENT_ID, MEASUREMENT_DATE_TIME, MEASUREMENT_VALUE, METER_ID));
    }
}
