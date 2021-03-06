package com.company.energy.service;

import com.company.energy.dao.AbstractResourceDAO;
import com.company.energy.dao.ResourceDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;

import java.io.IOException;
import java.util.List;
import com.company.energy.model.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.company.energy.util.Constants.*;
import static com.company.energy.util.Constants.RESOURCES_JSP;

public class GetResourcesService extends AbstractService {
    private static final AbstractResourceDAO resourceDAO = ResourceDAO.getInstance();

    private static GetResourcesService instance;

    private GetResourcesService() throws DAOException {};

    public static synchronized GetResourcesService getInstance() throws DAOException {
        if (instance == null) {
            instance = new GetResourcesService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            List<Resource> resources = resourceDAO.getAll();

            saveLastServiceURL(RESOURCES_URL_LAST_STATE, request);
            request.setAttribute(RESOURCES_ATTRIBUTE, resources);
            request.getRequestDispatcher(RESOURCES_JSP).forward(request, response);
        } catch (ServletException | IOException | DAOException e) {
            throw new ServiceException(e);
        }
    }
}
