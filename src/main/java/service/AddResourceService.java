package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import model.Resource;
import validator.ResourceValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import static util.Constants.*;
import static util.ServicesAllowedInputParametersLists.addResourceServiceAllowedInputParameters;

public class AddResourceService extends AbstractService {
    private static AddResourceService instance;

    private AddResourceService() throws DAOException{}

    public static synchronized AddResourceService getInstance() throws DAOException {
        if (instance==null) {
            instance = new AddResourceService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, addResourceServiceAllowedInputParameters);

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

        String resourceIdString = parameters.get(RESOURCE_ID)[0];
        String resourceNameString = parameters.get(RESOURCE_NAME)[0];
        String resourceCostString = parameters.get(RESOURCE_COST)[0];

        Long resourceId = resourceValidator.validateId(resourceIdString, allowEmpty);
        String resourceName = resourceValidator.validateName(resourceNameString, !allowEmpty);
        Double resourceCost = resourceValidator.validateCost(resourceCostString, !allowEmpty);

        Resource resource = new Resource();
        resource.setId(resourceId);
        resource.setName(resourceName);
        resource.setCost(resourceCost);

        return resource;
    }
}
