package com.company.energy.validator;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ValidationException;
import com.company.energy.util.Constants;

public class ResourceValidator extends Validator {
    private static ResourceValidator instance;

    private ResourceValidator() {}

    public static synchronized ResourceValidator getInstance() {
        if (instance == null) {
            instance = new ResourceValidator();
        }

        return instance;
    }

    public Long validateAndGetId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("resourceId");

        return validateAndGetLongField(id, fieldName, allowEmpty);
    }

    public String validateAndGetName(String name, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("resourceName");

        return validateAndGetStringField(name, Constants.STRING_MAX_LENGTH, fieldName, allowEmpty);
    }


    public Double validateAndGetCost(String cost, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("resourceCost");

        return validateAndGetDoubleField(cost, fieldName, allowEmpty);
    }
}
