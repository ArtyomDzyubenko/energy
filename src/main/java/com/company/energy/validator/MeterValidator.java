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

    public Long validateAndGetId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("meterId");

        return validateAndGetLongField(id, fieldName, allowEmpty);
    }

    public Integer validateAndGetNumber(String number, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("meterNumber");

        return validateAndGetIntField(number, fieldName, allowEmpty);
    }
}
