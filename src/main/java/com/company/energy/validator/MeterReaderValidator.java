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

    public Long validateAndGetId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("meterReaderId");

        return validateAndGetLongField(id, fieldName, allowEmpty);
    }

    public Integer validateAndGetNumber(String number, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("meterReaderNumber");

        return validateAndGetIntField(number, fieldName, allowEmpty);
    }

    public String validateAndGetIPAddress(String IPAddress, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("IPAddress");

        return validateAndGetStringField(IPAddress, Constants.IP_ADDRESS_STRING_MAX_LENGTH, Constants.IP_ADDRESS_REGEX, fieldName, allowEmpty);
    }

    public Integer validateAndGetPort(String number, boolean allowEmpty) throws ValidationException, DAOException {
        Integer out = validateAndGetIntField(number, getErrorLocalization("port"), allowEmpty);

        if (out < Constants.MIN_TCP_PORT_NUMBER || out > Constants.MAX_TCP_PORT_NUMBER) {
            String errorMessage =
                    getErrorLocalization("outOfRange") + " " +
                    Constants.MIN_TCP_PORT_NUMBER + " - " + Constants.MAX_TCP_PORT_NUMBER + " : " +
                    getErrorLocalization("port");

            throw new ValidationException(errorMessage);
        }

        return out;
    }
}
