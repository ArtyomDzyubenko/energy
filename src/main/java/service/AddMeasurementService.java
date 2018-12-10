package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.Measurement;
import validator.MeasurementValidator;
import validator.MeterValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Map;
import static util.Constants.*;
import static util.ServicesAllowedInputParametersLists.addMeasurementServiceAllowedInputParameters;

public class AddMeasurementService extends AbstractService {
    private static AddMeasurementService instance;

    private AddMeasurementService() throws DAOException{}

    public static synchronized AddMeasurementService getInstance() throws DAOException {
        if (instance==null) {
            instance = new AddMeasurementService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, addMeasurementServiceAllowedInputParameters);

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

        dateTimeString = dateTimeString.replace("T", " ");
        dateTime = measurementValidator.validateDate(dateTimeString, !allowEmpty);

        measurement.setDateTime(dateTime);
        measurement.setValue(measurementValue);
        measurement.setMeterId(meterId);

        return measurement;
    }
}
