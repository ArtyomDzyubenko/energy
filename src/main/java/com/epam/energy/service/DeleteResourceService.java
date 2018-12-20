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
import static com.epam.energy.util.Constants.RESOURCES_URL_LAST_STATE;
import static com.epam.energy.util.Constants.RESOURCE_ID;

public class DeleteResourceService extends AbstractService {
    private static DeleteResourceService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private DeleteResourceService() throws DAOException{
        init();
    }

    public static synchronized DeleteResourceService getInstance() throws DAOException {
        if (instance == null) {
            instance = new DeleteResourceService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long resourceId = getResourceId(parameters, !allowEmpty);

            resourceDAO.deleteResource(resourceId);

            response.sendRedirect(getLastServiceURL(RESOURCES_URL_LAST_STATE, request));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.add(RESOURCE_ID);
    }
}
