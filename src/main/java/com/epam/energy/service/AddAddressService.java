package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import java.io.IOException;
import com.epam.energy.model.Address;
import com.epam.energy.validator.AddressValidator;
import com.epam.energy.validator.ServiceParametersValidator;
import com.epam.energy.validator.StreetValidator;
import com.epam.energy.validator.UserValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import static com.epam.energy.util.Constants.*;

public class AddAddressService extends AbstractService {
    private static AddAddressService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private AddAddressService() throws DAOException {
        init();
    }

    public static synchronized AddAddressService getInstance() throws DAOException {
        if (instance == null) {
            instance = new AddAddressService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Address address = getAddress(parameters);

            if (address.getId().equals(LONG_ZERO)) {
                addressDAO.addAddressByUserId(address);
            } else {
                addressDAO.editAddress(address);
            }

            response.sendRedirect(getLastServiceURL(ADDRESSES_URL_LAST_STATE, request));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private Address getAddress(Map<String, String[]> parameters) throws ServiceException {
        AddressValidator addressValidator = AddressValidator.getInstance();
        StreetValidator streetValidator = StreetValidator.getInstance();
        UserValidator userValidator = UserValidator.getInstance();

        try {
            Long addressId = getAddressId(parameters);
            String addressBuilding = addressValidator.validateBuilding(parameters.get(ADDRESS_BUILDING)[0], !allowEmpty);
            String addressFlat = addressValidator.validateFlat(parameters.get(ADDRESS_FLAT)[0], allowEmpty);
            Long addressStreetId = streetValidator.validateId(parameters.get(STREET_ID)[0], !allowEmpty);
            Long userId;

            Address address = new Address();
            address.setId(addressId);
            address.setBuilding(addressBuilding);
            address.setFlat(addressFlat);
            address.getStreet().setId(addressStreetId);

            if (parameters.containsKey(TRANSFER_USER_ID)) {
                userId = userValidator.validateId(parameters.get(TRANSFER_USER_ID)[0], !allowEmpty);
                address.setUserId(userId);
            } else if (parameters.containsKey(USER_ID)) {
                userId = userValidator.validateId(parameters.get(USER_ID)[0], !allowEmpty);
                address.setUserId(userId);
            }

            return address;
        } catch (ValidationException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(ADDRESS_ID, ADDRESS_BUILDING, ADDRESS_FLAT, USER_ID, STREET_ID, TRANSFER_USER_ID));
    }
}
