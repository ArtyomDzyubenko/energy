package com.epam.energy.validator;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ValidationException;
import java.sql.Timestamp;

public class MeasurementValidator extends AbstractValidator {
    private static MeasurementValidator instance;

    private MeasurementValidator() {}

    public static synchronized MeasurementValidator getInstance() {
        if(instance==null){
            instance = new MeasurementValidator();
        }

        return instance;
    }

    public Long validateId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("measurementId");

        return validateLongField(id, fieldName, allowEmpty);
    }

    public Timestamp validateDate(String date, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("date");

        return validateDateTimeField(date, fieldName, allowEmpty);
    }

    public Double validateValue(String value, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("value");

        return validateDoubleField(value, fieldName, allowEmpty);
    }
}
