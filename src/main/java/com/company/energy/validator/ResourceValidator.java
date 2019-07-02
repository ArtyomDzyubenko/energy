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

    public Long validateId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("resourceId");

        return validateLongField(id, fieldName, allowEmpty);
    }

    public String validateName(String name, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("resourceName");

        return validateStringField(name, Constants.STRING_MAX_LENGTH, fieldName, allowEmpty);
    }


    public Double validateCost(String cost, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("resourceCost");

        return validateDoubleField(cost, fieldName, allowEmpty);
    }
}
