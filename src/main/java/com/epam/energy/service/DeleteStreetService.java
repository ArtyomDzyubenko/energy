package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import com.epam.energy.validator.ServiceParametersValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.STREETS_URL_LAST_STATE;
import static com.epam.energy.util.Constants.STREET_ID;

public class DeleteStreetService extends AbstractService {
    private static DeleteStreetService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private DeleteStreetService() throws DAOException{
        init();
    }

    public static synchronized DeleteStreetService getInstance() throws DAOException {
        if (instance == null) {
            instance = new DeleteStreetService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long streetId = getStreetId(parameters, !allowEmpty);

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
