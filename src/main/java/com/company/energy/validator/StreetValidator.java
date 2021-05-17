package com.company.energy.validator;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ValidationException;
import com.company.energy.util.Constants;

public class StreetValidator extends Validator {
    private static StreetValidator instance;

    private StreetValidator() {}

    public static synchronized StreetValidator getInstance() {
        if (instance == null) {
            instance = new StreetValidator();
        }

        return instance;
    }

    public Long validateAndGetId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("streetId");

        return validateAndGetLongField(id,  fieldName, allowEmpty);
    }

    public String validateAndGetName(String name, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("streetName");

        return validateAndGetStringField(name, Constants.STRING_MAX_LENGTH, fieldName, allowEmpty);
    }
}
