package com.company.energy.service;

import com.company.energy.dao.AbstractMeasurementDAO;
import com.company.energy.dao.MeasurementDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;

import java.io.IOException;
import com.company.energy.model.Measurement;
import com.company.energy.validator.MeasurementValidator;
import com.company.energy.validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;
import static com.company.energy.util.Constants.MEASUREMENTS_JSP;

public class EditMeasurementService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractMeasurementDAO measurementDAO = MeasurementDAO.getInstance();
    private static final MeasurementValidator measurementValidator = MeasurementValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static EditMeasurementService instance;

    private EditMeasurementService() throws DAOException {
        init();
    }

    public static synchronized EditMeasurementService getInstance() throws DAOException {
        if (instance == null) {
            instance = new EditMeasurementService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long measurementId = measurementValidator.validateAndGetId(parameters.get(MEASUREMENT_ID)[0], !allowEmpty);
            Measurement measurement = measurementDAO.getMeasurementById(measurementId).get(0);

            request.setAttribute(MEASUREMENT_ID, measurementId);
            request.setAttribute(MEASUREMENT_ATTRIBUTE, measurement);
            request.getRequestDispatcher(MEASUREMENTS_JSP).forward(request, response);
        } catch (ServletException | IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(MEASUREMENT_ID, METER_ID));
    }
}
