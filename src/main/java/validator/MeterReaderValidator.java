package validator;

import exception.DAOException;
import exception.ValidationException;
import util.Localization;

import static util.Constants.*;

public class MeterReaderValidator extends AbstractValidator {
    private static MeterReaderValidator instance;

    private MeterReaderValidator() {}

    public static synchronized MeterReaderValidator getInstance() {
        if(instance==null){
            instance = new MeterReaderValidator();
        }

        return instance;
    }

    public Long validateId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = Localization.getLocalization().getString("meterReaderId");
        return validateLongField(id, fieldName, allowEmpty);
    }

    public Integer validateNumber(String number, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = Localization.getLocalization().getString("meterReaderNumber");
        return validateIntField(number, fieldName, allowEmpty);
    }

    public String validateIPAddress(String IPAddress, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = Localization.getLocalization().getString("IPAddress");
        return validateStringField(IPAddress, IP_ADDRESS_STRING_MAX_LENGTH, IP_ADDRESS_REGEX, fieldName, allowEmpty);
    }

    public Integer validatePort(String number, boolean allowEmpty) throws ValidationException, DAOException {
        Integer out = validateIntField(number, Localization.getLocalization().getString("port"), allowEmpty);

        if (out < MIN_PORT_NUMBER || out > MAX_PORT_NUMBER){
            concat.setLength(0);
            String errorMessage = concat.append(Localization.getLocalization().getString("outOfRange")).append(" ")
                    .append(MIN_PORT_NUMBER).append(" - ").append(MAX_PORT_NUMBER).append(" : ").append(Localization.getLocalization().getString("port")).toString();
            throw new ValidationException(errorMessage);
        }

        return out;
    }
}
