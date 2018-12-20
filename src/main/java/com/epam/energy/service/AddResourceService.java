package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import com.epam.energy.model.Resource;
import com.epam.energy.validator.ResourceValidator;
import com.epam.energy.validator.ServiceParametersValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.*;

public class AddResourceService extends AbstractService {
    private static AddResourceService instance;
    private List<String> allowedParameters = new ArrayList<>();

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
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Resource resource = getResource(parameters);

            if (resource.getId().equals(LONG_ZERO)) {
                resourceDAO.addResource(resource);
            } else {
                resourceDAO.editResource(resource);
            }

            response.sendRedirect(getLastServiceURL(RESOURCES_URL_LAST_STATE, request));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private Resource getResource(Map<String, String[]> parameters) throws ValidationException, DAOException {
        ResourceValidator resourceValidator = ResourceValidator.getInstance();

        Long resourceId = getResourceId(parameters, allowEmpty);
        String resourceName = resourceValidator.validateName(parameters.get(RESOURCE_NAME)[0], !allowEmpty);
        Double resourceCost = resourceValidator.validateCost(parameters.get(RESOURCE_COST)[0], !allowEmpty);

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
