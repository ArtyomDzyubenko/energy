package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import model.Meter;
import validator.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static util.Constants.*;
import static util.Constants.ADDRESS_ID;

public class AddMeterService extends AbstractService {
    private static AddMeterService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private AddMeterService() throws DAOException {
        init();
    }

    public static synchronized AddMeterService getInstance() throws DAOException {
        if (instance==null){
            instance = new AddMeterService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Meter meter = getMeter(parameters);

            if (meter.getId().equals(LONG_ZERO)) {
                meterDAO.addMeterByAddressId(meter);
            } else {
                meterDAO.editMeter(meter);
            }

            response.sendRedirect(getLastServiceURL(METERS_URL_LAST_STATE, request));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private Meter getMeter(Map<String, String[]> parameters) throws ValidationException, DAOException {
        MeterValidator meterValidator = MeterValidator.getInstance();
        MeterReaderValidator meterReaderValidator = MeterReaderValidator.getInstance();
        AddressValidator addressValidator = AddressValidator.getInstance();
        ResourceValidator resourceValidator = ResourceValidator.getInstance();

        String meterNumberString = parameters.get(METER_NUMBER)[0];
        String meterReaderIdString = parameters.get(METER_READER_ID)[0];
        String resourceIdString = parameters.get(RESOURCE_ID)[0];
        String addressIdString;

        Long meterId = getMeterId(parameters);
        Integer meterNumber = meterValidator.validateNumber(meterNumberString, !allowEmpty);
        Long meterReaderId = meterReaderValidator.validateId(meterReaderIdString, !allowEmpty);
        Long resourceId = resourceValidator.validateId(resourceIdString, !allowEmpty);

        Meter meter = new Meter();
        meter.setId(meterId);
        meter.setNumber(meterNumber);
        meter.setMeterReaderId(meterReaderId);
        meter.getResource().setId(resourceId);
        Long addressId;

        if(parameters.containsKey(TRANSFER_ADDRESS_ID)){
            addressIdString = parameters.get(TRANSFER_ADDRESS_ID)[0];
            addressId = addressValidator.validateId(addressIdString, !allowEmpty);
            meter.setAddressId(addressId);
        } else if (parameters.containsKey(ADDRESS_ID)) {
            addressIdString = parameters.get(ADDRESS_ID)[0];
            addressId = addressValidator.validateId(addressIdString, !allowEmpty);
            meter.setAddressId(addressId);
        }

        return meter;
    }

    private void init(){
        allowedParameters.add(METER_ID);
        allowedParameters.add(METER_NUMBER);
        allowedParameters.add(METER_READER_ID);
        allowedParameters.add(RESOURCE_ID);
        allowedParameters.add(ADDRESS_ID);
        allowedParameters.add(TRANSFER_ADDRESS_ID);
    }
}
