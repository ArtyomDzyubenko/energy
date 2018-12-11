package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.Address;
import model.Street;
import validator.ServiceParametersValidator;
import validator.UserValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static util.Constants.*;

public class GetAddressesService extends AbstractService {
    private static GetAddressesService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private GetAddressesService() throws DAOException {
        init();
    }

    public static synchronized GetAddressesService getInstance() throws DAOException {
        if (instance==null){
            instance = new GetAddressesService();
        }

        return  instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();
            UserValidator userValidator = UserValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            String userIdString = parameters.get(USER_ID)[0];
            Long userId = userValidator.validateId(userIdString, !allowEmpty);

            String sKey = parameters.get(SECRET_KEY)[0];
            String authUserSessionId = AuthService.getInstance().getAuthUserSessionId();
            parametersValidator.validateSecretKey(userId.toString(), authUserSessionId, sKey);

            List<Address> address = addressDAO.getAddressesByUserId(userId);
            List<Street> streets = streetDAO.getAll();

            saveLastServiceURL(ADDRESSES_URL_LAST_STATE, request);
            request.setAttribute(USER_ID, userId);
            request.setAttribute(ADDRESSES_ATTRIBUTE_NAME, address);
            request.setAttribute(STREETS_ATTRIBUTE_NAME, streets);
            request.getRequestDispatcher(ADDRESSES_JSP).forward(request, response);
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
        allowedParameters.add(USER_ID);
        allowedParameters.add(SECRET_KEY);
    }
}
