package com.company.energy.service;

import com.company.energy.dao.*;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import java.io.IOException;
import com.company.energy.model.Invoice;
import com.company.energy.model.Measurement;
import com.company.energy.model.Meter;
import com.company.energy.model.User;
import com.company.energy.validator.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;

public class AddInvoiceService extends AbstractService {
    private static final StringBuilder concat = new StringBuilder();
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractInvoiceDAO invoiceDAO = InvoiceDAO.getInstance();
    private static final AbstractMeasurementDAO measurementDAO = MeasurementDAO.getInstance();
    private static final AbstractMeterDAO meterDAO = MeterDAO.getInstance();
    private static final AbstractUserDAO userDAO = UserDAO.getInstance();
    private static final UserValidator userValidator = UserValidator.getInstance();
    private static final MeasurementValidator measurementValidator = MeasurementValidator.getInstance();
    private static final InvoiceValidator invoiceValidator = InvoiceValidator.getInstance();
    private static final AddressValidator addressValidator = AddressValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static AddInvoiceService instance;

    private AddInvoiceService() throws DAOException{
        init();
    }

    public static synchronized AddInvoiceService getInstance() throws DAOException {
        if (instance == null) {
           instance = new AddInvoiceService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Invoice invoice = getInvoice(parameters);
            User user = userDAO.getUserById(invoice.getUserId()).get(0);
            invoiceDAO.addInvoiceByUserId(invoice);

            String redirectURL = concat.append(request.getContextPath()).append(SHOW_INVOICES).append("?")
                    .append(USER_ID).append("=").append(user.getId()).append("&").append(SECRET_KEY).append("=")
                    .append(user.getSecretKey()).toString();

            response.sendRedirect(redirectURL);
        } catch (IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private Invoice getInvoice(Map<String, String[]> parameters) throws ValidationException, DAOException {
        Long invoiceId = invoiceValidator.validateAndGetId(parameters.get(INVOICE_ID)[0], allowEmpty);
        Long userId = userValidator.validateAndGetId(parameters.get(USER_ID)[0], !allowEmpty);
        Long startValueId = measurementValidator.validateAndGetId(parameters.get(MEASUREMENT_START_VALUE)[0], !allowEmpty);
        Long endValueId = measurementValidator.validateAndGetId(parameters.get(MEASUREMENT_END_VALUE)[0], !allowEmpty);
        Long addressId = addressValidator.validateAndGetId(parameters.get(ADDRESS_ID)[0], !allowEmpty);

        Meter meter = meterDAO.getMetersByAddressId(addressId).get(0);
        Measurement startMeasurement = measurementDAO.getMeasurementById(startValueId).get(0);
        Measurement endMeasurement = measurementDAO.getMeasurementById(endValueId).get(0);
        invoiceValidator.validateAndGetDateTimePeriod(startMeasurement.getDateTime(), endMeasurement.getDateTime());

        double consumption = endMeasurement.getValue() - startMeasurement.getValue();
        double price = consumption * meter.getResource().getCost();

        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setDate(LocalDate.now());
        invoice.setMeter(meter);
        invoice.setStartValue(startMeasurement);
        invoice.setEndValue(endMeasurement);
        invoice.setConsumption(invoiceValidator.validateAndGetConsumption(consumption));
        invoice.setPrice(invoiceValidator.validateAndGetPrice(price));
        invoice.setUserId(userId);
        invoice.setPaid(false);

        return invoice;
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(MEASUREMENT_START_VALUE, MEASUREMENT_END_VALUE, USER_ID, METER_ID, INVOICE_ID, ADDRESS_ID));
    }
}
