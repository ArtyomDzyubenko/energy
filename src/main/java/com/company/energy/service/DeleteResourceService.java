package com.company.energy.service;

import com.company.energy.dao.AbstractResourceDAO;
import com.company.energy.dao.ResourceDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.validator.ResourceValidator;
import com.company.energy.validator.ServiceParametersValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.RESOURCES_URL_LAST_STATE;
import static com.company.energy.util.Constants.RESOURCE_ID;

public class DeleteResourceService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractResourceDAO resourceDAO = ResourceDAO.getInstance();
    private static final ResourceValidator resourceValidator = ResourceValidator.getInstance();

    private static DeleteResourceService instance;

    private DeleteResourceService() throws DAOException {
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

            Long resourceId = resourceValidator.validateAndGetId(parameters.get(RESOURCE_ID)[0], !allowEmpty);

            resourceDAO.deleteResource(resourceId);

            response.sendRedirect(getLastServiceURL(RESOURCES_URL_LAST_STATE, request));
        } catch (IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.add(RESOURCE_ID);
    }
}
