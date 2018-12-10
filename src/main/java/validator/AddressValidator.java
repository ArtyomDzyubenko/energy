package validator;

import exception.DAOException;
import exception.ValidationException;
import util.Localization;
import static util.Constants.*;

public class AddressValidator extends AbstractValidator {
    private static AddressValidator instance;

    private AddressValidator() { }

    public static synchronized AddressValidator getInstance(){
        if(instance==null){
            instance = new AddressValidator();
        }

        return instance;
    }

    public Long validateId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = Localization.getLocalization().getString("addressId");
        return validateLongField(id, fieldName, allowEmpty);
    }

    public String validateBuilding(String building, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = Localization.getLocalization().getString("building");
        return validateStringField(building, BUILDING_STRING_MAX_LENGTH, fieldName, allowEmpty);
    }

    public String validateFlat(String flat, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = Localization.getLocalization().getString("flat");
        return validateStringField(flat, FLAT_STRING_MAX_LENGTH, fieldName, allowEmpty);
    }
}
