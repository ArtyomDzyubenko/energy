package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.MeterEntity;
import model.MeterReader;
import model.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import static util.Constants.*;
import static util.Constants.METERS_JSP;
import static util.ServicesAllowedInputParametersLists.getMetersServiceAllowedInputParameters;

public class GetMetersService extends AbstractService {
    private static GetMetersService instance;

    private GetMetersService() throws DAOException { }

    public static synchronized GetMetersService getInstance() throws DAOException {
        if (instance==null){
            instance = new GetMetersService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, getMetersServiceAllowedInputParameters);

            Long addressId = getAddressId(parameters);
            Long userId = getUserId(parameters);

            String sKey = request.getParameter(SECRET_KEY);
            String authUserSessionId = AuthService.getInstance().getAuthUserSessionId();
            parametersValidator.validateSecretKey(addressId.toString(), authUserSessionId, sKey);

            List<MeterEntity> meters = meterDAO.getMetersByAddressId(addressId);
            List<Resource> resources = resourceDAO.getAll();
            List<MeterReader> readers = meterReaderDAO.getAll();

            saveLastServiceURL(METERS_URL_LAST_STATE, request);
            request.setAttribute(USER_ID, userId);
            request.setAttribute(ADDRESS_ID, addressId);
            request.setAttribute(METERS_ATTRIBUTE_NAME, meters);
            request.setAttribute(RESOURCES_ATTRIBUTE_NAME, resources);
            request.setAttribute(METER_READERS_ATTRIBUTE_NAME, readers);
            request.getRequestDispatcher(METERS_JSP).forward(request, response);
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
