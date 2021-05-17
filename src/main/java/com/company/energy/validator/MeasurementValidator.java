package com.company.energy.validator;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ValidationException;

import java.sql.Timestamp;

public class MeasurementValidator extends Validator {
    private static MeasurementValidator instance;

    private MeasurementValidator() {}

    public static synchronized MeasurementValidator getInstance() {
        if (instance == null) {
            instance = new MeasurementValidator();
        }

        return instance;
    }

    public Long validateAndGetId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("measurementId");

        return validateAndGetLongField(id, fieldName, allowEmpty);
    }

    public Timestamp validateAndGetDate(String date, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("date");

        return validateAndGetDateTimeField(date, fieldName, allowEmpty);
    }

    public Double validateAndGetValue(String value, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("value");

        return validateAndGetDoubleField(value, fieldName, allowEmpty);
    }
}
