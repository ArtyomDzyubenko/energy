package validator;

import exception.DAOException;
import exception.ValidationException;
import static util.Constants.*;

public class StreetValidator extends AbstractValidator {
    private static StreetValidator instance;

    private StreetValidator() {}

    public static synchronized StreetValidator getInstance() {
        if(instance==null){
            instance = new StreetValidator();
        }

        return instance;
    }

    public Long validateId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("streetId");

        return validateLongField(id,  fieldName, allowEmpty);
    }

    public String validateName(String name, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("streetName");

        return validateStringField(name, STRING_MAX_LENGTH, fieldName, allowEmpty);
    }
}
