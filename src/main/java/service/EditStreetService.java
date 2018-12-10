package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.Street;
import validator.StreetValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static util.Constants.*;
import static util.ServicesAllowedInputParametersLists.editStreetServiceAllowedInputParameters;

public class EditStreetService extends AbstractService {
    private static EditStreetService instance;

    private EditStreetService() throws DAOException{}

    public static synchronized EditStreetService getInstance() throws DAOException {
        if (instance==null) {
            instance = new EditStreetService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            StreetValidator streetValidator = StreetValidator.getInstance();
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, editStreetServiceAllowedInputParameters);

            String streetIdString = parameters.get(STREET_ID)[0];
            Long streetId = streetValidator.validateId(streetIdString, !allowEmpty);

            Street street = streetDAO.getStreetById(streetId).get(0);

            request.setAttribute(STREET_ATTRIBUTE_NAME, street);
            request.getRequestDispatcher(STREETS_JSP).forward(request, response);
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
