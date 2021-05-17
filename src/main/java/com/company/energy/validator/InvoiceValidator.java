package com.company.energy.validator;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ValidationException;

import java.sql.Timestamp;

public class InvoiceValidator extends Validator {
    private static InvoiceValidator instance;

    private InvoiceValidator() {}

    public static synchronized InvoiceValidator getInstance() {
        if (instance == null) {
            instance = new InvoiceValidator();
        }

        return instance;
    }

    public Long validateAndGetId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("invoiceId");

        return validateAndGetLongField(id, fieldName, allowEmpty);
    }

    public Double validateAndGetConsumption(double consumption) throws ValidationException, DAOException {
        if (consumption <= 0) {
            String errorMessage = getErrorLocalization("negativeValueError") + " " + getErrorLocalization("consumption");

            throw new ValidationException(errorMessage);
        }

        return consumption;
    }

    public Double validateAndGetPrice(double price) throws ValidationException, DAOException {
        if (price <= 0) {
            String errorMessage = getErrorLocalization("negativeValueError") + " " + getErrorLocalization("price");

            throw new ValidationException(errorMessage);
        }

        return price;
    }

    public void validateAndGetDateTimePeriod(Timestamp start, Timestamp end) throws ValidationException, DAOException {
        if (start.getTime() > end.getTime()) {
            throw new ValidationException(getErrorLocalization("incorrectDateTimePeriod"));
        }
    }
}
