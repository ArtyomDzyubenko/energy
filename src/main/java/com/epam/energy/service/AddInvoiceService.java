package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import java.io.IOException;
import com.epam.energy.model.Invoice;
import com.epam.energy.model.Measurement;
import com.epam.energy.model.Meter;
import com.epam.energy.model.User;
import com.epam.energy.validator.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.*;

public class AddInvoiceService extends AbstractService {
    private static AddInvoiceService instance;
    private static StringBuilder concat = new StringBuilder();
    private List<String> allowedParameters = new ArrayList<>();

    private AddInvoiceService() throws DAOException{
        init();
    }

    public static synchronized AddInvoiceService getInstance() throws DAOException {
        if (instance==null){
           instance = new AddInvoiceService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Invoice invoice = getInvoice(parameters);
            User user = userDAO.getUserById(invoice.getUserId()).get(0);
            invoiceDAO.addInvoiceByUserId(invoice);

            String redirectURL = concat.append(request.getContextPath()).append(SHOW_INVOICES).append("?")
                    .append(USER_ID).append("=").append(user.getId()).append("&").append(SECRET_KEY).append("=")
                    .append(user.getSecretKey()).toString();

            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private Invoice getInvoice(Map<String, String[]> parameters) throws ValidationException, DAOException {
        UserValidator userValidator = UserValidator.getInstance();
        MeasurementValidator measurementValidator = MeasurementValidator.getInstance();
        InvoiceValidator invoiceValidator = InvoiceValidator.getInstance();
        AddressValidator addressValidator = AddressValidator.getInstance();

        String invoiceIdString = parameters.get(INVOICE_ID)[0];
        String userIdString = parameters.get(USER_ID)[0];
        String startValueIdString = parameters.get(MEASUREMENT_START_VALUE)[0];
        String endValueIdString = parameters.get(MEASUREMENT_END_VALUE)[0];
        String addressIdString = parameters.get(ADDRESS_ID)[0];

        Long invoiceId = invoiceValidator.validateId(invoiceIdString, allowEmpty);
        Long userId = userValidator.validateId(userIdString, !allowEmpty);
        Long startValueId = measurementValidator.validateId(startValueIdString, !allowEmpty);
        Long endValueId = measurementValidator.validateId(endValueIdString, !allowEmpty);
        Long addressId = addressValidator.validateId(addressIdString, !allowEmpty);

        Meter meter = meterDAO.getMetersByAddressId(addressId).get(0);
        Measurement startMeasurement = measurementDAO.getMeasurementById(startValueId).get(0);
        Measurement endMeasurement = measurementDAO.getMeasurementById(endValueId).get(0);
        invoiceValidator.validateDateTimePeriod(startMeasurement.getDateTime(), endMeasurement.getDateTime());

        double consumption = endMeasurement.getValue() - startMeasurement.getValue();
        double price = consumption * meter.getResource().getCost();

        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setDate(LocalDate.now());
        invoice.setMeter(meter);
        invoice.setStartValue(startMeasurement);
        invoice.setEndValue(endMeasurement);
        invoice.setConsumption(invoiceValidator.validateConsumption(consumption));
        invoice.setPrice(invoiceValidator.validatePrice(price));
        invoice.setUserId(userId);
        invoice.setPaid(false);

        return invoice;
    }

    private void init() {
        allowedParameters.add(MEASUREMENT_START_VALUE);
        allowedParameters.add(MEASUREMENT_END_VALUE);
        allowedParameters.add(USER_ID);
        allowedParameters.add(MEASUREMENT_START_VALUE);
        allowedParameters.add(METER_ID);
        allowedParameters.add(INVOICE_ID);
        allowedParameters.add(ADDRESS_ID);
    }
}
