package com.company.energy.service;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.validator.ServiceParametersValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.METER_READERS_URL_LAST_STATE;
import static com.company.energy.util.Constants.METER_READER_ID;

public class DeleteMeterReaderService extends AbstractService {
    private static DeleteMeterReaderService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private DeleteMeterReaderService() throws DAOException {
        init();
    }

    public static synchronized DeleteMeterReaderService getInstance() throws DAOException {
        if (instance == null) {
            instance = new DeleteMeterReaderService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long meterReaderId = getMeterReaderId(parameters, !allowEmpty);

            meterReaderDAO.deleteMeterReader(meterReaderId);

            response.sendRedirect(getLastServiceURL(METER_READERS_URL_LAST_STATE, request));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.add(METER_READER_ID);
    }
}
