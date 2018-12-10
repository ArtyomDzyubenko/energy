package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.Street;
import validator.StreetValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static util.Constants.*;
import static util.ServicesAllowedInputParametersLists.addStreetServiceAllowedInputParameters;

public class AddStreetService extends AbstractService {
    private static AddStreetService instance;

    private AddStreetService() throws DAOException{}

    public static synchronized AddStreetService getInstance() throws DAOException {
        if (instance==null){
            instance = new AddStreetService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, addStreetServiceAllowedInputParameters);

            Street street = getStreet(parameters);

            if (street.getId().equals(LONG_ZERO)) {
                streetDAO.addStreet(street);
            } else {
                streetDAO.editStreet(street);
            }

            response.sendRedirect(getLastServiceURL(STREETS_URL_LAST_STATE, request));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private Street getStreet(Map<String, String[]> parameters) throws ValidationException, DAOException {
        StreetValidator streetValidator = StreetValidator.getInstance();

        String streetIdString = parameters.get(STREET_ID)[0];
        String streetNameString = parameters.get(STREET_NAME)[0];

        Long streetId = streetValidator.validateId(streetIdString, allowEmpty);
        String streetName = streetValidator.validateName(streetNameString, !allowEmpty);

        Street street = new Street();
        street.setId(streetId);
        street.setName(streetName);

        return street;
    }
}
