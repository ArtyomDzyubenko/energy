package com.company.energy.service;

import com.company.energy.dao.AbstractMeterDAO;
import com.company.energy.dao.MeterDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.validator.MeterValidator;
import com.company.energy.validator.ServiceParametersValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class DeleteMeterService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractMeterDAO meterDAO = MeterDAO.getInstance();
    private static final MeterValidator meterValidator = MeterValidator.getInstance();

    private static DeleteMeterService instance;

    private DeleteMeterService() throws DAOException {
        init();
    }

    public static synchronized DeleteMeterService getInstance() throws DAOException {
        if (instance == null) {
            instance = new DeleteMeterService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long meterId = meterValidator.validateAndGetId(parameters.get(METER_ID)[0], !allowEmpty);

            meterDAO.deleteMeter(meterId);

            response.sendRedirect(getLastServiceURL(METERS_URL_LAST_STATE, request));
        } catch (IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.add(METER_ID);
    }
}
