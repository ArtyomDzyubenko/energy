package com.company.energy.validator;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ValidationException;
import com.company.energy.util.Constants;

public class AddressValidator extends Validator {
    private static AddressValidator instance;

    private AddressValidator() { }

    public static synchronized AddressValidator getInstance() {
        if (instance == null) {
            instance = new AddressValidator();
        }

        return instance;
    }

    public Long validateAndGetId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("addressId");
        
        return validateAndGetLongField(id, fieldName, allowEmpty);
    }

    public String validateAndGetBuilding(String building, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("building");
        
        return validateAndGetStringField(building, Constants.ADDRESS_BUILDING_STRING_MAX_LENGTH, fieldName, allowEmpty);
    }

    public String validateAndGetFlat(String flat, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("flat");
        
        return validateAndGetStringField(flat, Constants.ADDRESS_FLAT_STRING_MAX_LENGTH, fieldName, allowEmpty);
    }
}
