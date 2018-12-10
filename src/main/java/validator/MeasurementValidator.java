package validator;

import exception.DAOException;
import exception.ValidationException;
import util.Localization;
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
        String fieldName = Localization.getLocalization().getString("measurementId");
        return validateLongField(id, fieldName, allowEmpty);
    }

    public Timestamp validateDate(String date, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = Localization.getLocalization().getString("date");
        return validateDateTimeField(date, fieldName, allowEmpty);
    }

    public Double validateValue(String value, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = Localization.getLocalization().getString("value");
        return validateDoubleField(value, fieldName, allowEmpty);
    }
}
