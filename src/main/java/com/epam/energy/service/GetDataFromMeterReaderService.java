package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import java.io.IOException;
import com.epam.energy.model.Meter;
import com.epam.energy.model.MeterReader;
import com.epam.energy.util.MeterReaderSocket;
import com.epam.energy.validator.MeterReaderValidator;
import com.epam.energy.validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.*;

public class GetDataFromMeterReaderService extends AbstractService {
    private static GetDataFromMeterReaderService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private GetDataFromMeterReaderService() throws DAOException{
        init();
    }

    public static synchronized GetDataFromMeterReaderService getInstance() throws DAOException {
        if (instance == null) {
            instance = new GetDataFromMeterReaderService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();
            MeterReaderValidator meterReaderValidator = MeterReaderValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            String meterReaderIdString = parameters.get(METER_READER_ID)[0];
            Long meterReaderId = meterReaderValidator.validateId(meterReaderIdString, !allowEmpty);

            MeterReader meterReader = meterReaderDAO.getMeterReaderById(meterReaderId).get(0);
            MeterReaderSocket socket = MeterReaderSocket.getInstance();
            List<Meter> meters = socket.getMetersFromMeterReader(meterReader.getIPAddress(), meterReader.getPort());
            measurementDAO.addMeasurements(meters);

            request.setAttribute(METERS_ATTRIBUTE, meters);
            request.getRequestDispatcher(METER_READER_DATA).forward(request, response);
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
