package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import java.io.IOException;
import com.epam.energy.model.Measurement;
import com.epam.energy.validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.*;
import static com.epam.energy.util.Constants.MEASUREMENTS_JSP;

public class EditMeasurementService extends AbstractService {
    private static EditMeasurementService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private EditMeasurementService() throws DAOException {
        init();
    }

    public static synchronized EditMeasurementService getInstance() throws DAOException {
        if (instance==null){
            instance = new EditMeasurementService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long measurementId = getMeasurementId(parameters);
            Measurement measurement = measurementDAO.getMeasurementById(measurementId).get(0);

            request.setAttribute(MEASUREMENT_ID, measurementId);
            request.setAttribute(MEASUREMENT_ATTRIBUTE_NAME, measurement);
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
        allowedParameters.add(MEASUREMENT_ID);
        allowedParameters.add(METER_ID);
    }
}
