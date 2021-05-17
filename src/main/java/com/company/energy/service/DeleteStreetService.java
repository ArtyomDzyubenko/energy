package com.company.energy.service;

import com.company.energy.dao.AbstractStreetDAO;
import com.company.energy.dao.StreetDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.validator.ServiceParametersValidator;
import com.company.energy.validator.StreetValidator;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.STREETS_URL_LAST_STATE;
import static com.company.energy.util.Constants.STREET_ID;

public class DeleteStreetService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractStreetDAO streetDAO = StreetDAO.getInstance();
    private static final StreetValidator streetValidator = StreetValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static DeleteStreetService instance;

    private DeleteStreetService() throws DAOException {
        init();
    }

    public static synchronized DeleteStreetService getInstance() throws DAOException {
        if (instance == null) {
            instance = new DeleteStreetService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long streetId = streetValidator.validateAndGetId(parameters.get(STREET_ID)[0], !allowEmpty);

            streetDAO.deleteStreetById(streetId);

            response.sendRedirect(getLastServiceURL(STREETS_URL_LAST_STATE, request));
        } catch (IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.add(STREET_ID);
    }
}
