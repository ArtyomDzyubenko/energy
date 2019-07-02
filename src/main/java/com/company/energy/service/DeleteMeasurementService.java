package com.company.energy.service;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.validator.ServiceParametersValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.MEASUREMENTS_URL_LAST_STATE;
import static com.company.energy.util.Constants.MEASUREMENT_ID;

public class DeleteMeasurementService extends AbstractService {
    private static DeleteMeasurementService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private DeleteMeasurementService() throws DAOException {
        init();
    }

    public static synchronized DeleteMeasurementService getInstance() throws DAOException {
        if (instance == null) {
            instance = new DeleteMeasurementService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long measurementId = getMeasurementId(parameters, !allowEmpty);

            measurementDAO.deleteMeasurementById(measurementId);

            response.sendRedirect(getLastServiceURL(MEASUREMENTS_URL_LAST_STATE, request));
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
    }
}
