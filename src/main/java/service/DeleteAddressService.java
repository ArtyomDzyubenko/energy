package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import validator.AddressValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static util.Constants.ADDRESSES_URL_LAST_STATE;
import static util.Constants.ADDRESS_ID;
import static util.ServicesAllowedInputParametersLists.deleteAddressServiceAllowedInputParameters;

public class DeleteAddressService extends AbstractService {
    private static DeleteAddressService instance;

    private DeleteAddressService() throws DAOException {}

    public static synchronized DeleteAddressService getInstance() throws DAOException {
        if (instance==null){
            instance = new DeleteAddressService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, deleteAddressServiceAllowedInputParameters);

            Long addressId = getAddressId(parameters);

            addressDAO.deleteAddressById(addressId);

            response.sendRedirect(getLastServiceURL(ADDRESSES_URL_LAST_STATE, request));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }
}
