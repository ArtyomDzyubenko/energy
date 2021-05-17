package com.company.energy.service;

import com.company.energy.dao.AbstractAddressDAO;
import com.company.energy.dao.AddressDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.validator.AddressValidator;
import com.company.energy.validator.ServiceParametersValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.ADDRESSES_URL_LAST_STATE;
import static com.company.energy.util.Constants.ADDRESS_ID;

public class DeleteAddressService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractAddressDAO addressDAO = AddressDAO.getInstance();
    private static final AddressValidator addressValidator = AddressValidator.getInstance();

    private static DeleteAddressService instance;

    private DeleteAddressService() throws DAOException {
        init();
    }

    public static synchronized DeleteAddressService getInstance() throws DAOException {
        if (instance == null) {
            instance = new DeleteAddressService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long addressId = addressValidator.validateAndGetId(parameters.get(ADDRESS_ID)[0], !allowEmpty);

            addressDAO.deleteAddressById(addressId);

            response.sendRedirect(getLastServiceURL(ADDRESSES_URL_LAST_STATE, request));
        } catch (IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.add(ADDRESS_ID);
    }
}
