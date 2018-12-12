package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import java.io.IOException;
import com.epam.energy.model.MeterReader;
import com.epam.energy.validator.MeterReaderValidator;
import com.epam.energy.validator.ServiceParametersValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.*;

public class AddMeterReaderService extends AbstractService {
    private static AddMeterReaderService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private AddMeterReaderService() throws DAOException{
        init();
    }

    public static synchronized  AddMeterReaderService getInstance() throws DAOException {
        if (instance == null) {
            instance = new AddMeterReaderService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            MeterReader meterReader = getMeterReader(parameters);

            if (meterReader.getId().equals(LONG_ZERO)) {
                meterReaderDAO.addMeterReader(meterReader);
            } else {
                meterReaderDAO.editMeterReader(meterReader);
            }

            response.sendRedirect(getLastServiceURL(METER_READERS_URL_LAST_STATE, request));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private MeterReader getMeterReader(Map<String, String[]> parameters) throws ValidationException, DAOException {
        MeterReaderValidator meterReaderValidator = MeterReaderValidator.getInstance();

        String meterReaderIdString = parameters.get(METER_READER_ID)[0];
        String meterReaderNumberString = parameters.get(METER_READER_NUMBER)[0];
        String meterReaderIPAddressString = parameters.get(METER_READER_IP_ADDRESS)[0];
        String meterReaderPortString = parameters.get(METER_READER_PORT)[0];

        Long meterReaderId = meterReaderValidator.validateId(meterReaderIdString, allowEmpty);
        Integer meterReaderNumber = meterReaderValidator.validateNumber(meterReaderNumberString, !allowEmpty);
        String meterReaderIPAddress = meterReaderValidator.validateIPAddress(meterReaderIPAddressString, !allowEmpty);
        Integer meterReaderPort = meterReaderValidator.validatePort(meterReaderPortString, !allowEmpty);

        MeterReader meterReader = new MeterReader();
        meterReader.setId(meterReaderId);
        meterReader.setNumber(meterReaderNumber);
        meterReader.setIPAddress(meterReaderIPAddress);
        meterReader.setPort(meterReaderPort);

        return meterReader;
    }

    private void init() {
        allowedParameters.add(METER_READER_ID);
        allowedParameters.add(METER_READER_NUMBER);
        allowedParameters.add(METER_READER_IP_ADDRESS);
        allowedParameters.add(METER_READER_PORT);
    }
}
