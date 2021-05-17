package com.company.energy.validator;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ValidationException;
import com.company.energy.service.LanguageService;
import com.company.energy.util.Constants;

import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Pattern;

public class Validator {
    public String validateAndGetStringField(String field, int length, String fieldName, boolean allowEmpty) throws ValidationException, DAOException {
        if (checkForEmptyField(field, fieldName, allowEmpty)) {
            return Constants.EMPTY_STRING;
        }

        checkFieldLength(field, fieldName, length);

        return field;
    }

    public String validateAndGetStringField(String field, int length, String regex, String fieldName, boolean allowEmpty) throws ValidationException, DAOException {
        if (checkForEmptyField(field, fieldName, allowEmpty)) {
            return Constants.EMPTY_STRING;
        }

        checkFieldLength(field, fieldName, length);

        boolean match = Pattern.compile(regex).matcher(field).matches();

        if (match) {
            return field;
        } else {
            String errorMessage = getErrorLocalization("incorrectSymbols") + " " + fieldName;

            throw new ValidationException(errorMessage);
        }
    }

    public Long validateAndGetLongField(String field, String fieldName, boolean allowEmpty) throws ValidationException, DAOException {
        if (checkForEmptyField(field, fieldName, allowEmpty)) {
            return Constants.LONG_ZERO;
        }

        Long out;

        try {
            out = Long.parseLong(field);
        } catch (NumberFormatException e) {
             String errorMessage = getValueNotANumberErrorString(fieldName);

            throw new ValidationException(errorMessage);
        }

        checkNumberForNegativeValue(out, fieldName);

        return out;
    }

    public Integer validateAndGetIntField(String field, String fieldName, boolean allowEmpty) throws ValidationException, DAOException {
        if (checkForEmptyField(field, fieldName, allowEmpty)) {
            return Constants.INT_ZERO;
        }

        Integer out;

        try {
            out = Integer.parseInt(field);
        } catch (NumberFormatException e) {
            String errorMessage = getValueNotANumberErrorString(fieldName);

            throw new ValidationException(errorMessage);
        }

        checkNumberForNegativeValue(out, fieldName);

        return out;
    }

    public Double validateAndGetDoubleField(String field, String fieldName, boolean allowEmpty) throws ValidationException, DAOException {
        if (checkForEmptyField(field, fieldName, allowEmpty)) {
            return Constants.DOUBLE_ZERO;
        }

        Double out;

        try {
            out = Double.parseDouble(field);
        } catch (NumberFormatException e) {
            String errorMessage = getValueNotANumberErrorString(fieldName);

            throw new ValidationException(errorMessage);
        }

        checkNumberForNegativeValue(out, fieldName);

        return out;
    }

    public Timestamp validateAndGetDateTimeField(String field, String fieldName, boolean allowEmpty) throws ValidationException, DAOException {
        if (checkForEmptyField(field, fieldName, allowEmpty)) {
            return new Timestamp(new Date().getTime());
        }

        Timestamp out;

        try {
            out = Timestamp.valueOf(field);
        } catch (IllegalArgumentException e) {
            String errorMessage = getErrorLocalization("incorrectDateTimeFormat") + " " + fieldName;

            throw new ValidationException(errorMessage);
        }

        return out;
    }

    private boolean checkForEmptyField(String field, String fieldName, boolean allowEmpty) throws ValidationException, DAOException {
        if (field.isEmpty() && allowEmpty) {
            return true;
        } else if (field.isEmpty()) {
            String errorMessage = getErrorLocalization("emptyNotAllowed") + " " + fieldName;

            throw new ValidationException(errorMessage);
        }

        return false;
    }

    private void checkFieldLength(String field, String fieldName, int length) throws ValidationException, DAOException {
        if (field.length() > length) {
            String errorMessage = getErrorLocalization("stringLengthError") + " " + length + " : " + fieldName;

            throw new ValidationException(errorMessage);
        }
    }

    private void checkNumberForNegativeValue(Number value, String fieldName) throws ValidationException, DAOException {
        if ((value instanceof Long && value.longValue() < 0) ||
            (value instanceof Double && value.doubleValue() < 0) ||
            (value instanceof Integer && value.intValue() < 0)) {

            String errorMessage = getErrorLocalization("negativeValueError") + " " + fieldName;

            throw new ValidationException(errorMessage);
        }
    }

    private String getValueNotANumberErrorString(String fieldName) throws DAOException {
        return getErrorLocalization("valueNotANumberError") + " " + fieldName;
    }

    String getErrorLocalization(String key) throws DAOException {
        return LanguageService.getInstance().getLocalization().getString(key);
    }
}



