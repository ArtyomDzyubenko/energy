package com.epam.energy.service;

import com.epam.energy.dao.*;
import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import com.epam.energy.validator.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static com.epam.energy.util.Constants.*;

public abstract class AbstractService {
    AbstractAddressDAO addressDAO = AddressDAO.getInstance();
    AbstractInvoiceDAO invoiceDAO = InvoiceDAO.getInstance();
    AbstractLanguageDAO languageDAO = LanguageDAO.getInstance();
    AbstractMeasurementDAO measurementDAO = MeasurementDAO.getInstance();
    AbstractMeterDAO meterDAO = MeterDAO.getInstance();
    AbstractMeterReaderDAO meterReaderDAO = MeterReaderDAO.getInstance();
    AbstractResourceDAO resourceDAO = ResourceDAO.getInstance();
    AbstractStreetDAO streetDAO = StreetDAO.getInstance();
    AbstractUserDAO userDAO = UserDAO.getInstance();
    boolean allowEmpty = true;

    AbstractService() throws DAOException { }

    public abstract void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException;

    void saveLastServiceURL(String URL, HttpServletRequest request) {
        String queryString = request.getQueryString();
        String lastURL;

        if (queryString != null) {
            lastURL = request.getRequestURI() + "?" + queryString;
        } else {
            lastURL = request.getRequestURI();
        }

        request.getSession().setAttribute(LAST_URL, lastURL);
        request.getSession().setAttribute(URL, lastURL);
    }

    String getLastServiceURL(String URL, HttpServletRequest request) {
        return (String)request.getSession().getAttribute(URL);
    }

    Long getAddressId(Map<String, String[]> parameters) throws ValidationException, DAOException {
        AddressValidator addressValidator = AddressValidator.getInstance();
        String addressIdString = parameters.get(ADDRESS_ID)[0];

        return addressValidator.validateId(addressIdString, allowEmpty);
    }

    Long getMeterId(Map<String, String[]> parameters) throws ValidationException, DAOException {
        MeterValidator meterValidator = MeterValidator.getInstance();
        String meterIdString = parameters.get(METER_ID)[0];

        return meterValidator.validateId(meterIdString, allowEmpty);
    }

    Long getUserId(Map<String, String[]> parameters) throws ValidationException, DAOException {
        UserValidator userValidator = UserValidator.getInstance();
        String userIdString = parameters.get(USER_ID)[0];

        return userValidator.validateId(userIdString, allowEmpty);
    }

    Long getInvoiceId(Map<String, String[]> parameters) throws ValidationException, DAOException {
        InvoiceValidator invoiceValidator = InvoiceValidator.getInstance();
        String invoiceIdString = parameters.get(INVOICE_ID)[0];

        return invoiceValidator.validateId(invoiceIdString, allowEmpty);
    }

    Long getMeasurementId(Map<String, String[]> parameters) throws ValidationException, DAOException {
        MeasurementValidator measurementValidator = MeasurementValidator.getInstance();
        String measurementIdString = parameters.get(MEASUREMENT_ID)[0];

        return measurementValidator.validateId(measurementIdString, allowEmpty);
    }

    Long getMeterReaderId(Map<String, String[]> parameters) throws ValidationException, DAOException {
        MeterReaderValidator meterReaderValidator = MeterReaderValidator.getInstance();
        String meterReaderIdString = parameters.get(METER_READER_ID)[0];

        return meterReaderValidator.validateId(meterReaderIdString, allowEmpty);
    }
}
