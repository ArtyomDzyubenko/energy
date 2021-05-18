package com.company.energy.service;

import com.company.energy.dao.AbstractMeasurementDAO;
import com.company.energy.dao.AbstractMeterReaderDAO;
import com.company.energy.dao.MeasurementDAO;
import com.company.energy.dao.MeterReaderDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;

import java.io.IOException;
import com.company.energy.model.Meter;
import com.company.energy.model.MeterReader;
import com.company.energy.util.Encryption;
import com.company.energy.util.MeterReaderSocket;
import com.company.energy.validator.MeterReaderValidator;
import com.company.energy.validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class GetDataFromMeterReaderService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractMeasurementDAO measurementDAO = MeasurementDAO.getInstance();
    private static final AbstractMeterReaderDAO meterReaderDAO = MeterReaderDAO.getInstance();
    private static final MeterReaderValidator meterReaderValidator = MeterReaderValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static GetDataFromMeterReaderService instance;

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
            Map<String, String[]> parameters = request.getParameterMap();

            parametersValidator.validate(parameters, allowedParameters);

            Long meterReaderId = meterReaderValidator.validateAndGetId(parameters.get(METER_READER_ID)[0], !allowEmpty);

            MeterReader meterReader = meterReaderDAO.getMeterReaderById(meterReaderId).get(0);
            MeterReaderSocket socket = MeterReaderSocket.getInstance();
            List<Meter> meters = socket.getMetersFromMeterReader(meterReader.getIPAddress(), meterReader.getPort());
            meters.forEach(meter -> meter.setSecretKey(Encryption.encrypt(meter.getId() + request.getSession().getId())));

            measurementDAO.addMeasurements(meters);

            request.setAttribute(METERS_ATTRIBUTE, meters);
            request.getRequestDispatcher(METER_READER_DATA).forward(request, response);
        } catch (ServletException | IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.add(METER_READER_ID);
    }
}
