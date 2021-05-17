package com.company.energy.service;

import com.company.energy.dao.AbstractStreetDAO;
import com.company.energy.dao.StreetDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import java.io.IOException;
import com.company.energy.model.Street;
import com.company.energy.validator.ServiceParametersValidator;
import com.company.energy.validator.StreetValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class EditStreetService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractStreetDAO streetDAO = StreetDAO.getInstance();
    private static final StreetValidator streetValidator = StreetValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();


    private static EditStreetService instance;

    private EditStreetService() throws DAOException{
        init();
    }

    public static synchronized EditStreetService getInstance() throws DAOException {
        if (instance == null) {
            instance = new EditStreetService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long streetId = streetValidator.validateAndGetId(parameters.get(STREET_ID)[0], !allowEmpty);

            Street street = streetDAO.getStreetById(streetId).get(0);

            request.setAttribute(STREET_ATTRIBUTE, street);
            request.getRequestDispatcher(STREETS_JSP).forward(request, response);
        } catch (ServletException | IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.add(STREET_ID);
    }
}
