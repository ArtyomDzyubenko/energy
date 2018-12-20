package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import com.epam.energy.model.Meter;
import com.epam.energy.validator.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.*;
import static com.epam.energy.util.Constants.ADDRESS_ID;

public class AddMeterService extends AbstractService {
    private static AddMeterService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private AddMeterService() throws DAOException {
        init();
    }

    public static synchronized AddMeterService getInstance() throws DAOException {
        if (instance == null) {
            instance = new AddMeterService();
        }

        return instance;
    }

    @Override
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
        AddressValidator addressValidator = AddressValidator.getInstance();
        ResourceValidator resourceValidator = ResourceValidator.getInstance();

        Long meterId = getMeterId(parameters, allowEmpty);
        Integer meterNumber = meterValidator.validateNumber(parameters.get(METER_NUMBER)[0], !allowEmpty);
        Long meterReaderId = getMeterReaderId(parameters, !allowEmpty);
        Long resourceId = resourceValidator.validateId(parameters.get(RESOURCE_ID)[0], !allowEmpty);

        Meter meter = new Meter();
        meter.setId(meterId);
        meter.setNumber(meterNumber);
        meter.setMeterReaderId(meterReaderId);
        meter.getResource().setId(resourceId);
        Long addressId;

        if (parameters.containsKey(TRANSFER_ADDRESS_ID)) {
            addressId = addressValidator.validateId(parameters.get(TRANSFER_ADDRESS_ID)[0], !allowEmpty);
            meter.setAddressId(addressId);
        } else if (parameters.containsKey(ADDRESS_ID)) {
            addressId = getAddressId(parameters, !allowEmpty);
            meter.setAddressId(addressId);
        }

        return meter;
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(METER_ID, METER_NUMBER, METER_READER_ID, RESOURCE_ID, ADDRESS_ID, TRANSFER_ADDRESS_ID));
    }
}
