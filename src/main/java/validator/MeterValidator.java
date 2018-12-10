package validator;

import exception.DAOException;
import exception.ValidationException;
import util.Localization;

public class MeterValidator extends AbstractValidator {
    private static MeterValidator instance;

    private MeterValidator() {}

    public static synchronized MeterValidator getInstance() {
        if(instance==null){
            instance = new MeterValidator();
        }

        return instance;
    }

    public Long validateId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = Localization.getLocalization().getString("meterId");
        return validateLongField(id, fieldName, allowEmpty);
    }

    public Integer validateNumber(String number, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = Localization.getLocalization().getString("meterNumber");
        return validateIntField(number, fieldName, allowEmpty);
    }
}
