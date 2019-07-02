package com.company.energy.service;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;

import java.io.IOException;
import com.company.energy.model.Resource;
import com.company.energy.validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class EditResourceService extends AbstractService {
    private static EditResourceService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private EditResourceService() throws DAOException {
        init();
    }

    public static synchronized EditResourceService getInstance() throws DAOException {
        if (instance == null) {
            instance = new EditResourceService();
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
            Resource resource = resourceDAO.getResourceById(resourceId).get(0);

            request.setAttribute(RESOURCE_ATTRIBUTE, resource);
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
