package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.Address;
import validator.AddressValidator;
import validator.StreetValidator;
import validator.UserValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static util.Constants.*;
import static util.ServicesAllowedInputParametersLists.addAddressServiceAllowedInputParameters;

public class AddAddressService extends AbstractService {
    private static AddAddressService instance;

    private AddAddressService() throws DAOException {}

    public static synchronized AddAddressService getInstance() throws DAOException {
        if (instance==null){
            instance = new AddAddressService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, addAddressServiceAllowedInputParameters);

            Address address = getAddress(parameters);

            if (address.getId().equals(LONG_ZERO)){
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

        String addressBuildingString = parameters.get(ADDRESS_BUILDING)[0];
        String addressFlatString = parameters.get(ADDRESS_FLAT)[0];
        String addressStreetIdString = parameters.get(STREET_ID)[0];
        String userIdString;

        try {
            Long addressId = getAddressId(parameters);
            String addressBuilding = addressValidator.validateBuilding(addressBuildingString, !allowEmpty);
            String addressFlat = addressValidator.validateFlat(addressFlatString, allowEmpty);
            Long addressStreetId = streetValidator.validateId(addressStreetIdString, !allowEmpty);
            Long userId;

            Address address = new Address();
            address.setId(addressId);
            address.setBuilding(addressBuilding);
            address.setFlat(addressFlat);
            address.getStreet().setId(addressStreetId);

            if (parameters.containsKey(TRANSFER_USER_ID)) {
                userIdString = parameters.get(TRANSFER_USER_ID)[0];
                userId = userValidator.validateId(userIdString, !allowEmpty);
                address.setUserId(userId);
            } else if (parameters.containsKey(USER_ID)) {
                userIdString = parameters.get(USER_ID)[0];
                userId = userValidator.validateId(userIdString, !allowEmpty);
                address.setUserId(userId);
            }

            return address;
        } catch (ValidationException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
