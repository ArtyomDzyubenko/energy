package com.company.energy.service;

import com.company.energy.dao.AbstractMeterDAO;
import com.company.energy.dao.MeterDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.model.Meter;
import com.company.energy.validator.AddressValidator;
import com.company.energy.validator.MeterValidator;
import com.company.energy.validator.ResourceValidator;
import com.company.energy.validator.ServiceParametersValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;
import static com.company.energy.util.Constants.ADDRESS_ID;

public class AddMeterService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractMeterDAO meterDAO = MeterDAO.getInstance();
    private static final MeterValidator meterValidator = MeterValidator.getInstance();
    private static final AddressValidator addressValidator = AddressValidator.getInstance();
    private static final ResourceValidator resourceValidator = ResourceValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static AddMeterService instance;

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
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Meter meter = getMeter(parameters);

            if (meter.getId().equals(LONG_ZERO)) {
                meterDAO.addMeterByAddressId(meter);
            } else {
                meterDAO.editMeter(meter);
            }

            response.sendRedirect(getLastServiceURL(METERS_URL_LAST_STATE, request));
        } catch (IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private Meter getMeter(Map<String, String[]> parameters) throws ValidationException, DAOException {
        Long meterId = meterValidator.validateAndGetId(parameters.get(METER_ID)[0], allowEmpty);
        Integer meterNumber = meterValidator.validateAndGetNumber(parameters.get(METER_NUMBER)[0], !allowEmpty);
        Long meterReaderId = meterValidator.validateAndGetId(parameters.get(METER_READER_ID)[0], !allowEmpty);
        Long resourceId = resourceValidator.validateAndGetId(parameters.get(RESOURCE_ID)[0], !allowEmpty);

        Meter meter = new Meter();
        meter.setId(meterId);
        meter.setNumber(meterNumber);
        meter.setMeterReaderId(meterReaderId);
        meter.getResource().setId(resourceId);
        Long addressId;

        if (parameters.containsKey(TRANSFER_ADDRESS_ID)) {
            addressId = addressValidator.validateAndGetId(parameters.get(TRANSFER_ADDRESS_ID)[0], !allowEmpty);
            meter.setAddressId(addressId);
        } else if (parameters.containsKey(ADDRESS_ID)) {
            addressId = addressValidator.validateAndGetId(parameters.get(ADDRESS_ID)[0], !allowEmpty);
            meter.setAddressId(addressId);
        }

        return meter;
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(METER_ID, METER_NUMBER, METER_READER_ID, RESOURCE_ID, ADDRESS_ID, TRANSFER_ADDRESS_ID));
    }
}
