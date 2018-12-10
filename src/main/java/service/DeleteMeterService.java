package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static util.Constants.*;
import static util.ServicesAllowedInputParametersLists.deleteMeterServiceAllowedInputParameters;

public class DeleteMeterService extends AbstractService {
    private static DeleteMeterService instance;

    private DeleteMeterService() throws DAOException {}

    public static synchronized DeleteMeterService getInstance() throws DAOException {
        if (instance==null){
            instance = new DeleteMeterService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, deleteMeterServiceAllowedInputParameters);

            Long meterId = getMeterId(parameters);

            meterDAO.deleteMeter(meterId);

            response.sendRedirect(getLastServiceURL(METERS_URL_LAST_STATE, request));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }
}
