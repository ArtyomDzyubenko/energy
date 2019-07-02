package com.company.energy.service;

import com.company.energy.dao.*;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.validator.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static com.company.energy.util.Constants.*;

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

    Long getAddressId(Map<String, String[]> parameters, boolean allowEmpty) throws ValidationException, DAOException {
        AddressValidator addressValidator = AddressValidator.getInstance();

        return addressValidator.validateId(parameters.get(ADDRESS_ID)[0], allowEmpty);
    }

    Long getMeterId(Map<String, String[]> parameters, boolean allowEmpty) throws ValidationException, DAOException {
        MeterValidator meterValidator = MeterValidator.getInstance();

        return meterValidator.validateId(parameters.get(METER_ID)[0], allowEmpty);
    }

    Long getUserId(Map<String, String[]> parameters, boolean allowEmpty) throws ValidationException, DAOException {
        UserValidator userValidator = UserValidator.getInstance();

        return userValidator.validateId(parameters.get(USER_ID)[0], allowEmpty);
    }

    Long getInvoiceId(Map<String, String[]> parameters, boolean allowEmpty) throws ValidationException, DAOException {
        InvoiceValidator invoiceValidator = InvoiceValidator.getInstance();

        return invoiceValidator.validateId(parameters.get(INVOICE_ID)[0], allowEmpty);
    }

    Long getMeasurementId(Map<String, String[]> parameters, boolean allowEmpty) throws ValidationException, DAOException {
        MeasurementValidator measurementValidator = MeasurementValidator.getInstance();

        return measurementValidator.validateId(parameters.get(MEASUREMENT_ID)[0], allowEmpty);
    }

    Long getMeterReaderId(Map<String, String[]> parameters, boolean allowEmpty) throws ValidationException, DAOException {
        MeterReaderValidator meterReaderValidator = MeterReaderValidator.getInstance();

        return meterReaderValidator.validateId(parameters.get(METER_READER_ID)[0], allowEmpty);
    }

    Long getResourceId(Map<String, String[]> parameters, boolean allowEmpty) throws ValidationException, DAOException {
        ResourceValidator resourceValidator = ResourceValidator.getInstance();

        return resourceValidator.validateId(parameters.get(RESOURCE_ID)[0], allowEmpty);
    }

    Long getStreetId(Map<String, String[]> parameters, boolean allowEmpty) throws ValidationException, DAOException {
        StreetValidator streetValidator = StreetValidator.getInstance();

        return streetValidator.validateId(parameters.get(STREET_ID)[0], allowEmpty);
    }

    String getUserLogin(Map<String, String[]> parameters) throws ValidationException, DAOException {
        UserValidator userValidator = UserValidator.getInstance();

        return userValidator.validateLogin(parameters.get(USER_LOGIN)[0], !allowEmpty);
    }

    String getUserPassword(Map<String, String[]> parameters) throws ValidationException, DAOException {
        UserValidator userValidator = UserValidator.getInstance();

        return userValidator.validatePassword(parameters.get(USER_PASSWORD)[0], !allowEmpty);
    }
}
