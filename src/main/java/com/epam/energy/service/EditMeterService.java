package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import java.io.IOException;
import com.epam.energy.model.Address;
import com.epam.energy.model.Meter;
import com.epam.energy.model.MeterReader;
import com.epam.energy.model.Resource;
import com.epam.energy.validator.AddressValidator;
import com.epam.energy.validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.*;
import static com.epam.energy.util.Constants.METERS_JSP;

public class EditMeterService extends AbstractService {
    private static EditMeterService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private EditMeterService() throws DAOException {
        init();
    }

    public static synchronized EditMeterService getInstance() throws DAOException {
        if (instance==null){
            instance = new EditMeterService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();
            AddressValidator addressValidator = AddressValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long meterId = getMeterId(parameters);
            String addressIdString = parameters.get(ADDRESS_ID)[0];
            Long addressId = addressValidator.validateId(addressIdString, !allowEmpty);

            List<Address> addresses = addressDAO.getAll();
            List<Resource> resources = resourceDAO.getAll();
            List<MeterReader> readers = meterReaderDAO.getAll();
            Meter meter = meterDAO.getMeter(meterId).get(0);

            request.setAttribute(ADDRESSES_ATTRIBUTE_NAME, addresses);
            request.setAttribute(METER_READER_ID, meterId);
            request.setAttribute(ADDRESS_ID, addressId);
            request.setAttribute(METER_ATTRIBUTE_NAME, meter);
            request.setAttribute(RESOURCES_ATTRIBUTE_NAME, resources);
            request.setAttribute(METER_READERS_ATTRIBUTE_NAME, readers);
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
        allowedParameters.add(METER_ID);
        allowedParameters.add(ADDRESS_ID);
    }
}
