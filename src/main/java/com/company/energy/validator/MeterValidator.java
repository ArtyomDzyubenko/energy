package com.company.energy.validator;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ValidationException;

public class MeterValidator extends Validator {
    private static MeterValidator instance;

    private MeterValidator() {}

    public static synchronized MeterValidator getInstance() {
        if (instance == null) {
            instance = new MeterValidator();
        }

        return instance;
    }

    public Long validateId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("meterId");

        return validateLongField(id, fieldName, allowEmpty);
    }

    public Integer validateNumber(String number, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("meterNumber");

        return validateIntField(number, fieldName, allowEmpty);
    }
}
