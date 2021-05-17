package com.company.energy.service;

import com.company.energy.dao.AbstractResourceDAO;
import com.company.energy.dao.ResourceDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;

import java.io.IOException;
import com.company.energy.model.Resource;
import com.company.energy.validator.ResourceValidator;
import com.company.energy.validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class EditResourceService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractResourceDAO resourceDAO = ResourceDAO.getInstance();
    private static final ResourceValidator resourceValidator = ResourceValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static EditResourceService instance;

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
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long resourceId = resourceValidator.validateAndGetId(parameters.get(RESOURCE_ID)[0], !allowEmpty);
            Resource resource = resourceDAO.getResourceById(resourceId).get(0);

            request.setAttribute(RESOURCE_ATTRIBUTE, resource);
            request.getRequestDispatcher(RESOURCES_JSP).forward(request, response);
        } catch (ServletException | IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.add(RESOURCE_ID);
    }
}
