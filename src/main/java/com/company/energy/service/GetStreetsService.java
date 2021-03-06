package com.company.energy.service;

import com.company.energy.dao.AbstractStreetDAO;
import com.company.energy.dao.StreetDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;

import java.io.IOException;
import java.util.List;
import com.company.energy.model.Street;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.company.energy.util.Constants.*;
import static com.company.energy.util.Constants.STREETS_JSP;

public class GetStreetsService extends AbstractService {
    private static final AbstractStreetDAO streetDAO = StreetDAO.getInstance();

    private static GetStreetsService instance;

    private GetStreetsService() throws DAOException {}

    public static synchronized GetStreetsService getInstance() throws DAOException {
        if (instance == null) {
            instance = new GetStreetsService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            List<Street> streets = streetDAO.getAll();

            saveLastServiceURL(STREETS_URL_LAST_STATE, request);
            request.setAttribute(STREETS_ATTRIBUTE, streets);
            request.getRequestDispatcher(STREETS_JSP).forward(request, response);
        } catch (ServletException | IOException | DAOException e) {
            throw new ServiceException(e);
        }
    }
}
