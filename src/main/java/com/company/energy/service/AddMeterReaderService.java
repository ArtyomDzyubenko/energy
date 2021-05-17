package com.company.energy.service;

import com.company.energy.dao.AbstractMeterReaderDAO;
import com.company.energy.dao.MeterReaderDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import java.io.IOException;
import com.company.energy.model.MeterReader;
import com.company.energy.validator.MeterReaderValidator;
import com.company.energy.validator.ServiceParametersValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class AddMeterReaderService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractMeterReaderDAO meterReaderDAO = MeterReaderDAO.getInstance();
    private static final MeterReaderValidator meterReaderValidator = MeterReaderValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static AddMeterReaderService instance;

    private AddMeterReaderService() throws DAOException{
        init();
    }

    public static synchronized  AddMeterReaderService getInstance() throws DAOException {
        if (instance == null) {
            instance = new AddMeterReaderService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            MeterReader meterReader = getMeterReader(parameters);

            if (meterReader.getId().equals(LONG_ZERO)) {
                meterReaderDAO.addMeterReader(meterReader);
            } else {
                meterReaderDAO.editMeterReader(meterReader);
            }

            response.sendRedirect(getLastServiceURL(METER_READERS_URL_LAST_STATE, request));
        } catch (IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private MeterReader getMeterReader(Map<String, String[]> parameters) throws ValidationException, DAOException {
        Long meterReaderId = meterReaderValidator.validateAndGetId(parameters.get(METER_READER_ID)[0], allowEmpty);
        Integer meterReaderNumber = meterReaderValidator.validateAndGetNumber(parameters.get(METER_READER_NUMBER)[0], !allowEmpty);
        String meterReaderIPAddress = meterReaderValidator.validateAndGetIPAddress(parameters.get(METER_READER_IP_ADDRESS)[0], !allowEmpty);
        Integer meterReaderPort = meterReaderValidator.validateAndGetPort(parameters.get(METER_READER_PORT)[0], !allowEmpty);

        MeterReader meterReader = new MeterReader();
        meterReader.setId(meterReaderId);
        meterReader.setNumber(meterReaderNumber);
        meterReader.setIPAddress(meterReaderIPAddress);
        meterReader.setPort(meterReaderPort);

        return meterReader;
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(METER_READER_ID, METER_READER_NUMBER, METER_READER_IP_ADDRESS, METER_READER_PORT));
    }
}
