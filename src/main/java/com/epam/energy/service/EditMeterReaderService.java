package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import com.epam.energy.model.MeterReader;
import com.epam.energy.validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.*;

public class EditMeterReaderService extends AbstractService {
    private static EditMeterReaderService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private EditMeterReaderService() throws DAOException{
        init();
    }

    public static synchronized EditMeterReaderService getInstance() throws DAOException {
        if (instance == null) {
            instance = new EditMeterReaderService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long meterReaderId = getMeterReaderId(parameters);

            MeterReader reader = meterReaderDAO.getMeterReaderById(meterReaderId).get(0);

            request.setAttribute(METER_READER_ATTRIBUTE, reader);
            request.getRequestDispatcher(METER_READERS_JSP).forward(request, response);
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
        allowedParameters.add(METER_READER_ID);
    }
}
