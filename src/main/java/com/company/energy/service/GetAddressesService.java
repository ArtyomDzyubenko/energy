package com.company.energy.service;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;

import java.io.IOException;
import com.company.energy.model.Address;
import com.company.energy.model.Street;
import com.company.energy.validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class GetAddressesService extends AbstractService {
    private static GetAddressesService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private GetAddressesService() throws DAOException {
        init();
    }

    public static synchronized GetAddressesService getInstance() throws DAOException {
        if (instance == null) {
            instance = new GetAddressesService();
        }

        return  instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();

            parametersValidator.validate(parameters, allowedParameters);

            Long userId = getUserId(parameters, !allowEmpty);
            String sKey = parameters.get(SECRET_KEY)[0];
            String authorizedUserSessionId = AuthService.getInstance().getAuthorizedUserSessionId();

            parametersValidator.validateSecretKey(userId.toString(), authorizedUserSessionId, sKey);

            List<Address> address = addressDAO.getAddressesByUserId(userId);
            List<Street> streets = streetDAO.getAll();

            saveLastServiceURL(ADDRESSES_URL_LAST_STATE, request);
            request.setAttribute(USER_ID, userId);
            request.setAttribute(ADDRESSES_ATTRIBUTE, address);
            request.setAttribute(STREETS_ATTRIBUTE, streets);
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
        allowedParameters.addAll(Arrays.asList(USER_ID, SECRET_KEY));
    }
}