package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.Measurement;
import validator.MeasurementValidator;
import validator.MeterValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import static util.Constants.*;
import static util.ServicesAllowedInputParametersLists.getMeasurementsServiceAllowedInputParameters;

public class GetMeasurementsService extends AbstractService {
    private static GetMeasurementsService instance;

    private GetMeasurementsService() throws DAOException {}

    public static synchronized GetMeasurementsService getInstance() throws DAOException {
        if (instance==null){
            instance = new GetMeasurementsService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, getMeasurementsServiceAllowedInputParameters);

            Long meterId = getMeterId(parameters);
            String secretKey = parameters.get(SECRET_KEY)[0];
            String authUserSessionId = AuthService.getInstance().getAuthUserSessionId();
            parametersValidator.validateSecretKey(meterId.toString(), authUserSessionId, secretKey);

            Long addressId = getAddressId(parameters);
            Long userId = getUserId(parameters);
            List<Measurement> measurements = measurementDAO.getMeasurementByMeterId(meterId);

            saveLastServiceURL(MEASUREMENTS_URL_LAST_STATE, request);
            request.setAttribute(METER_ID, meterId);
            request.setAttribute(ADDRESS_ID, addressId);
            request.setAttribute(USER_ID, userId);
            request.setAttribute(MEASUREMENTS_ATTRIBUTE_NAME, measurements);
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
