package service;

import exception.DAOException;
import exception.ServiceException;
import DAO.StreetDAO;
import DAO.UserDAO;
import exception.ValidationException;
import model.Address;
import model.Street;
import model.User;
import validator.AddressValidator;
import validator.UserValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import static util.Constants.*;
import static util.Constants.ADDRESSES_JSP;
import static util.ServicesAllowedInputParametersLists.editAddressServiceAllowedInputParameters;

public class EditAddressService extends AbstractService {
    private static EditAddressService instance;

    private EditAddressService() throws DAOException {}

    public static synchronized EditAddressService getInstance() throws DAOException {
        if (instance==null){
            instance = new EditAddressService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            UserValidator userValidator = UserValidator.getInstance();
            UserDAO userDAO = UserDAO.getInstance();
            StreetDAO streetDAO = StreetDAO.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, editAddressServiceAllowedInputParameters);

            String userIdString = parameters.get(USER_ID)[0];

            Long addressId = getAddressId(parameters);
            Long userId = userValidator.validateId(userIdString, !allowEmpty);

            Address address = addressDAO.getAddressById(addressId).get(0);
            List<Street> streets = streetDAO.getAll();
            List<User> users = userDAO.getAll();

            request.setAttribute(USER_ID, userId);
            request.setAttribute(ADDRESS_ATTRIBUTE_NAME, address);
            request.setAttribute(STREETS_ATTRIBUTE_NAME, streets);
            request.setAttribute(USERS_ATTRIBUTE_NAME, users);
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
}
