package com.company.energy.service;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;

import java.io.IOException;
import com.company.energy.model.Address;
import com.company.energy.model.Meter;
import com.company.energy.model.MeterReader;
import com.company.energy.model.Resource;
import com.company.energy.validator.AddressValidator;
import com.company.energy.validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;
import static com.company.energy.util.Constants.METERS_JSP;

public class EditMeterService extends AbstractService {
    private static EditMeterService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private EditMeterService() throws DAOException {
        init();
    }

    public static synchronized EditMeterService getInstance() throws DAOException {
        if (instance == null) {
            instance = new EditMeterService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();
            AddressValidator addressValidator = AddressValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long meterId = getMeterId(parameters, !allowEmpty);
            String addressIdString = parameters.get(ADDRESS_ID)[0];
            Long addressId = addressValidator.validateId(addressIdString, !allowEmpty);

            List<Address> addresses = addressDAO.getAll();
            List<Resource> resources = resourceDAO.getAll();
            List<MeterReader> readers = meterReaderDAO.getAll();
            Meter meter = meterDAO.getMeterById(meterId).get(0);

            request.setAttribute(ADDRESSES_ATTRIBUTE, addresses);
            request.setAttribute(METER_READER_ID, meterId);
            request.setAttribute(ADDRESS_ID, addressId);
            request.setAttribute(METER_ATTRIBUTE, meter);
            request.setAttribute(RESOURCES_ATTRIBUTE, resources);
            request.setAttribute(METER_READERS_ATTRIBUTE, readers);
            request.getRequestDispatcher(METERS_JSP).forward(request, response);
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
        allowedParameters.addAll(Arrays.asList(METER_ID, ADDRESS_ID));
    }
}
