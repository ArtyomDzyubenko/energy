package com.epam.energy.validator;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ValidationException;
import static com.epam.energy.util.Constants.*;

public class AddressValidator extends AbstractValidator {
    private static AddressValidator instance;

    private AddressValidator() { }

    public static synchronized AddressValidator getInstance() {
        if(instance==null){
            instance = new AddressValidator();
        }

        return instance;
    }

    public Long validateId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("addressId");
        
        return validateLongField(id, fieldName, allowEmpty);
    }

    public String validateBuilding(String building, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("building");
        
        return validateStringField(building, BUILDING_STRING_MAX_LENGTH, fieldName, allowEmpty);
    }

    public String validateFlat(String flat, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("flat");
        
        return validateStringField(flat, FLAT_STRING_MAX_LENGTH, fieldName, allowEmpty);
    }
}
