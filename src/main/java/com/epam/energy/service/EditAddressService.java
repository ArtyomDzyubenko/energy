package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import com.epam.energy.model.Address;
import com.epam.energy.model.Street;
import com.epam.energy.model.User;
import com.epam.energy.validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.*;
import static com.epam.energy.util.Constants.ADDRESSES_JSP;

public class EditAddressService extends AbstractService {
    private static EditAddressService instance;
    private List<String> allowedParameters = new ArrayList<>();


    private EditAddressService() throws DAOException {
        init();
    }

    public static synchronized EditAddressService getInstance() throws DAOException {
        if (instance == null) {
            instance = new EditAddressService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long userId = getUserId(parameters, !allowEmpty);
            Long addressId = getAddressId(parameters, !allowEmpty);

            Address address = addressDAO.getAddressById(addressId).get(0);
            List<Street> streets = streetDAO.getAll();
            List<User> users = userDAO.getAll();

            request.setAttribute(USER_ID, userId);
            request.setAttribute(ADDRESS_ATTRIBUTE, address);
            request.setAttribute(STREETS_ATTRIBUTE, streets);
            request.setAttribute(USERS_ATTRIBUTE, users);
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
        allowedParameters.addAll(Arrays.asList(ADDRESS_ID, USER_ID));
    }
}
