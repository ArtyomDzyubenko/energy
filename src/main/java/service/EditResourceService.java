package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.Resource;
import validator.ResourceValidator;
import validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static util.Constants.*;

public class EditResourceService extends AbstractService {
    private static EditResourceService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private EditResourceService() throws DAOException {
        init();
    }

    public static synchronized EditResourceService getInstance() throws DAOException {
        if (instance==null) {
            instance = new EditResourceService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();
            ResourceValidator resourceValidator = ResourceValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

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

    private void init() {
        allowedParameters.add(RESOURCE_ID);
    }
}
