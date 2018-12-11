package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import java.io.IOException;
import com.epam.energy.model.Measurement;
import com.epam.energy.validator.MeasurementValidator;
import com.epam.energy.validator.MeterValidator;
import com.epam.energy.validator.ServiceParametersValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.*;

public class AddMeasurementService extends AbstractService {
    private static AddMeasurementService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private AddMeasurementService() throws DAOException{
        init();
    }

    public static synchronized AddMeasurementService getInstance() throws DAOException {
        if (instance==null) {
            instance = new AddMeasurementService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Measurement measurement = getMeasurement(parameters);

            if (measurement.getId().equals(LONG_ZERO)) {
                measurementDAO.addMeasurement(measurement);
            } else {
                measurementDAO.editMeasurement(measurement);
            }

            response.sendRedirect(getLastServiceURL(MEASUREMENTS_URL_LAST_STATE, request));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private Measurement getMeasurement(Map<String, String[]> parameters) throws ValidationException, DAOException {
        MeasurementValidator measurementValidator = MeasurementValidator.getInstance();
        MeterValidator meterValidator = MeterValidator.getInstance();

        String measurementIdString = parameters.get(MEASUREMENT_ID)[0];
        String measurementValueString = parameters.get(MEASUREMENT_VALUE)[0];
        String meterIdString = parameters.get(METER_ID)[0];
        String dateTimeString = EMPTY_STRING;

        Long  measurementId = measurementValidator.validateId(measurementIdString, allowEmpty);
        Double measurementValue = measurementValidator.validateValue(measurementValueString, !allowEmpty);
        Long meterId = meterValidator.validateId(meterIdString, allowEmpty);
        Timestamp dateTime;

        Measurement measurement = new Measurement();
        measurement.setId(measurementId);

        if (parameters.containsKey(MEASUREMENT_LOCAL_DATE_TIME_STRING)){
            dateTimeString = parameters.get(MEASUREMENT_LOCAL_DATE_TIME_STRING)[0];
        } else if (parameters.containsKey(MEASUREMENT_DATE_TIME)) {
            dateTimeString = parameters.get(MEASUREMENT_DATE_TIME)[0];
        }

        dateTimeString = dateTimeString.replace(DATE_TIME_DELIMITER, SPACE);
        dateTime = measurementValidator.validateDate(dateTimeString, !allowEmpty);

        measurement.setDateTime(dateTime);
        measurement.setValue(measurementValue);
        measurement.setMeterId(meterId);

        return measurement;
    }

    private void init() {
        allowedParameters.add(MEASUREMENT_ID);
        allowedParameters.add(MEASUREMENT_DATE_TIME);
        allowedParameters.add(MEASUREMENT_VALUE);
        allowedParameters.add(METER_ID);
    }
}
