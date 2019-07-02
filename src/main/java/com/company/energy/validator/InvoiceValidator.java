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

    public Long validateId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("invoiceId");

        return validateLongField(id, fieldName, allowEmpty);
    }

    public Double validateConsumption(double consumption) throws ValidationException, DAOException {
        if (consumption <= 0) {
            concat.setLength(0);
            String errorMessage = concat.append(getErrorLocalization("negativeValueError")).append(" ")
                    .append(getErrorLocalization("consumption")).toString();

            throw new ValidationException(errorMessage);
        }

        return consumption;
    }

    public Double validatePrice(double price) throws ValidationException, DAOException {
        if (price <= 0) {
            concat.setLength(0);
            String errorMessage = concat.append(getErrorLocalization("negativeValueError")).append(" ")
                    .append(getErrorLocalization("price")).toString();

            throw new ValidationException(errorMessage);
        }

        return price;
    }

    public void validateDateTimePeriod(Timestamp start, Timestamp end) throws ValidationException, DAOException {
        if (start.getTime() > end.getTime()) {
            throw new ValidationException(getErrorLocalization("incorrectDateTimePeriod"));
        }
    }
}
