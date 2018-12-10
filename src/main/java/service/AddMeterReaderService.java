package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.MeterReader;
import validator.MeterReaderValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static util.Constants.*;
import static util.ServicesAllowedInputParametersLists.addMeterReaderServiceAllowedInputParameters;

public class AddMeterReaderService extends AbstractService {
    private static AddMeterReaderService instance;

    private AddMeterReaderService() throws DAOException{}

    public static synchronized  AddMeterReaderService getInstance() throws DAOException {
        if (instance==null){
            instance = new AddMeterReaderService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, addMeterReaderServiceAllowedInputParameters);

            MeterReader meterReader = getMeterReader(parameters);

            if(meterReader.getId().equals(LONG_ZERO)) {
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

        MeterReader meterReader = new MeterReader();
        meterReader.setId(meterReaderValidator.validateId(parameters.get(METER_READER_ID)[0], allowEmpty));
        meterReader.setNumber(meterReaderValidator.validateNumber(parameters.get(METER_READER_NUMBER)[0], !allowEmpty));
        meterReader.setIPAddress(meterReaderValidator.validateIPAddress(parameters.get(METER_READER_IP_ADDRESS)[0], !allowEmpty));
        meterReader.setPort(meterReaderValidator.validatePort(parameters.get(METER_READER_PORT)[0], !allowEmpty));

        return meterReader;
    }
}
