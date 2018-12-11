package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import com.epam.energy.validator.ServiceParametersValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.MEASUREMENTS_URL_LAST_STATE;
import static com.epam.energy.util.Constants.MEASUREMENT_ID;

public class DeleteMeasurementService extends AbstractService {
    private static DeleteMeasurementService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private DeleteMeasurementService() throws DAOException {
        init();
    }

    public static synchronized DeleteMeasurementService getInstance() throws DAOException {
        if (instance==null){
            instance = new DeleteMeasurementService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long measurementId = getMeasurementId(parameters);

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
