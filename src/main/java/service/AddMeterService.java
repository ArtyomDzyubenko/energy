package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import model.MeterEntity;
import validator.AddressValidator;
import validator.MeterReaderValidator;
import validator.MeterValidator;
import validator.ResourceValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import static util.Constants.*;
import static util.Constants.ADDRESS_ID;
import static util.ServicesAllowedInputParametersLists.addMeterServiceAllowedInputParameters;

public class AddMeterService extends AbstractService {
    private static AddMeterService instance;

    private AddMeterService() throws DAOException { }

    public static synchronized AddMeterService getInstance() throws DAOException {
        if (instance==null){
            instance = new AddMeterService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, addMeterServiceAllowedInputParameters);

            MeterEntity meter = getMeter(parameters);

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

    private MeterEntity getMeter(Map<String, String[]> parameters) throws ValidationException, DAOException {
        MeterValidator meterValidator = MeterValidator.getInstance();
        MeterReaderValidator meterReaderValidator = MeterReaderValidator.getInstance();
        ResourceValidator resourceValidator = ResourceValidator.getInstance();
        AddressValidator addressValidator = AddressValidator.getInstance();

        String meterNumberString = parameters.get(METER_NUMBER)[0];
        String meterReaderIdString = parameters.get(METER_READER_ID)[0];
        String resourceIdString = parameters.get(RESOURCE_ID)[0];

        Long meterId = getMeterId(parameters);
        Integer meterNumber = meterValidator.validateNumber(meterNumberString, !allowEmpty);
        Long meterReaderId = meterReaderValidator.validateId(meterReaderIdString, !allowEmpty);
        Long resourceId = resourceValidator.validateId(resourceIdString, !allowEmpty);

        MeterEntity meter = new MeterEntity();
        meter.setId(meterId);
        meter.setNumber(meterNumber);
        meter.setMeterReaderId(meterReaderId);
        meter.getResource().setId(resourceId);

        Long addressId;

        if(parameters.containsKey(TRANSFER_ADDRESS_ID)){
            String addressIdString = parameters.get(TRANSFER_ADDRESS_ID)[0];
            addressId = addressValidator.validateId(addressIdString, !allowEmpty);
            meter.setAddressId(addressId);
        } else if (parameters.containsKey(ADDRESS_ID)) {
            String addressIdString = parameters.get(ADDRESS_ID)[0];
            addressId = addressValidator.validateId(addressIdString, !allowEmpty);
            meter.setAddressId(addressId);
        }

        return meter;
    }
}
