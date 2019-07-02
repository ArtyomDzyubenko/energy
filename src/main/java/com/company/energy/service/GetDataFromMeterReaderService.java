package com.company.energy.service;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;

import java.io.IOException;
import com.company.energy.model.Meter;
import com.company.energy.model.MeterReader;
import com.company.energy.util.MeterReaderSocket;
import com.company.energy.validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class GetDataFromMeterReaderService extends AbstractService {
    private static GetDataFromMeterReaderService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private GetDataFromMeterReaderService() throws DAOException {
        init();
    }

    public static synchronized GetDataFromMeterReaderService getInstance() throws DAOException {
        if (instance == null) {
            instance = new GetDataFromMeterReaderService();
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
