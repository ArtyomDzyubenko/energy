package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.Resource;
import validator.ResourceValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static util.Constants.*;
import static util.ServicesAllowedInputParametersLists.editResourceServiceAllowedInputParameters;

public class EditResourceService extends AbstractService {
    private static EditResourceService instance;

    private EditResourceService() throws DAOException {}

    public static synchronized EditResourceService getInstance() throws DAOException {
        if (instance==null) {
            instance = new EditResourceService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ResourceValidator resourceValidator = ResourceValidator.getInstance();
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, editResourceServiceAllowedInputParameters);

            String resourceIdString = parameters.get(RESOURCE_ID)[0];
            Long resourceId = resourceValidator.validateId(resourceIdString, !allowEmpty);
            Resource resource = resourceDAO.getResourceById(resourceId).get(0);

            request.setAttribute(RESOURCE_ATTRIBUTE_NAME, resource);
            request.getRequestDispatcher(RESOURCES_JSP).forward(request, response);
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
