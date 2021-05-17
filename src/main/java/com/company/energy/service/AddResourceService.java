package com.company.energy.service;

import com.company.energy.dao.AbstractResourceDAO;
import com.company.energy.dao.ResourceDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.model.Resource;
import com.company.energy.validator.ResourceValidator;
import com.company.energy.validator.ServiceParametersValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class AddResourceService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractResourceDAO resourceDAO = ResourceDAO.getInstance();
    private static final ResourceValidator resourceValidator = ResourceValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static AddResourceService instance;

    private AddResourceService() throws DAOException{
        init();
    }

    public static synchronized AddResourceService getInstance() throws DAOException {
        if (instance == null) {
            instance = new AddResourceService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Resource resource = getResource(parameters);

            if (resource.getId().equals(LONG_ZERO)) {
                resourceDAO.addResource(resource);
            } else {
                resourceDAO.editResource(resource);
            }

            response.sendRedirect(getLastServiceURL(RESOURCES_URL_LAST_STATE, request));
        } catch (IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private Resource getResource(Map<String, String[]> parameters) throws ValidationException, DAOException {
        Long resourceId = resourceValidator.validateAndGetId(parameters.get(RESOURCE_ID)[0], allowEmpty);
        String resourceName = resourceValidator.validateAndGetName(parameters.get(RESOURCE_NAME)[0], !allowEmpty);
        Double resourceCost = resourceValidator.validateAndGetCost(parameters.get(RESOURCE_COST)[0], !allowEmpty);

        Resource resource = new Resource();
        resource.setId(resourceId);
        resource.setName(resourceName);
        resource.setCost(resourceCost);

        return resource;
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(RESOURCE_ID, RESOURCE_NAME, RESOURCE_COST));
    }
}
