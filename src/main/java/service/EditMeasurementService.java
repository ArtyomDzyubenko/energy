package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.Measurement;
import validator.MeasurementValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static util.Constants.*;
import static util.Constants.MEASUREMENTS_JSP;
import static util.ServicesAllowedInputParametersLists.editMeasurementServiceAllowedInputParameters;

public class EditMeasurementService extends AbstractService {
    private static EditMeasurementService instance;

    private EditMeasurementService() throws DAOException {}

    public static synchronized EditMeasurementService getInstance() throws DAOException {
        if (instance==null){
            instance = new EditMeasurementService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, editMeasurementServiceAllowedInputParameters);

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
}
