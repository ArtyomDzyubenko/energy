package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import validator.ServiceParametersValidator;
import validator.StreetValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static util.Constants.STREETS_URL_LAST_STATE;
import static util.Constants.STREET_ID;

public class DeleteStreetService extends AbstractService {
    private static DeleteStreetService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private DeleteStreetService() throws DAOException{
        init();
    }

    public static synchronized DeleteStreetService getInstance() throws DAOException {
        if (instance==null){
            instance = new DeleteStreetService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();
            StreetValidator streetValidator = StreetValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            String streetIdString = parameters.get(STREET_ID)[0];
            Long streetId = streetValidator.validateId(streetIdString, !allowEmpty);

            streetDAO.deleteStreetById(streetId);

            response.sendRedirect(getLastServiceURL(STREETS_URL_LAST_STATE, request));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.add(STREET_ID);
    }
}
