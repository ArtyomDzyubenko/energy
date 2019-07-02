package com.company.energy.validator;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ValidationException;
import com.company.energy.util.Constants;

public class MeterReaderValidator extends Validator {
    private static MeterReaderValidator instance;

    private MeterReaderValidator() {}

    public static synchronized MeterReaderValidator getInstance() {
        if (instance == null) {
            instance = new MeterReaderValidator();
        }

        return instance;
    }

    public Long validateId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("meterReaderId");

        return validateLongField(id, fieldName, allowEmpty);
    }

    public Integer validateNumber(String number, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("meterReaderNumber");

        return validateIntField(number, fieldName, allowEmpty);
    }

    public String validateIPAddress(String IPAddress, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("IPAddress");

        return validateStringField(IPAddress, Constants.IP_ADDRESS_STRING_MAX_LENGTH, Constants.IP_ADDRESS_REGEX, fieldName, allowEmpty);
    }

    public Integer validatePort(String number, boolean allowEmpty) throws ValidationException, DAOException {
        Integer out = validateIntField(number, getErrorLocalization("port"), allowEmpty);

        if (out < Constants.MIN_TCP_PORT_NUMBER || out > Constants.MAX_TCP_PORT_NUMBER) {
            concat.setLength(0);
            String errorMessage = concat.append(getErrorLocalization("outOfRange")).append(" ")
                    .append(Constants.MIN_TCP_PORT_NUMBER).append(" - ").append(Constants.MAX_TCP_PORT_NUMBER).append(" : ")
                    .append(getErrorLocalization("port")).toString();

            throw new ValidationException(errorMessage);
        }

        return out;
    }
}
