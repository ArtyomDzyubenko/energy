package validator;

import exception.DAOException;
import exception.ValidationException;
import util.Localization;
import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Pattern;
import static util.Constants.*;

abstract class AbstractValidator {
    StringBuilder concat = new StringBuilder();

    String validateStringField(String field, int length, String fieldName, boolean allowEmpty) throws ValidationException, DAOException {
        if(checkForEmptyField(field, fieldName, allowEmpty)){
            return EMPTY_STRING;
        }

        checkFieldLength(field, fieldName, length);

        return field;
    }

    String validateStringField(String field, int length, String regex, String fieldName, boolean allowEmpty) throws ValidationException, DAOException {
        if(checkForEmptyField(field, fieldName, allowEmpty)){
            return EMPTY_STRING;
        }

        checkFieldLength(field, fieldName, length);

        boolean match = Pattern.compile(regex).matcher(field).matches();

        if (match){
            return field;
        } else {
            concat.setLength(0);
            String errorMessage = concat.append(Localization.getLocalization()
                    .getString("incorrectSymbols")).append(" ").append(fieldName).toString();
            throw new ValidationException(errorMessage);
        }
    }

    Long validateLongField(String field, String fieldName, boolean allowEmpty) throws ValidationException, DAOException {
        if(checkForEmptyField(field, fieldName, allowEmpty)){
            return LONG_ZERO;
        }

        Long out;

        try {
            out = Long.parseLong(field);
        } catch (NumberFormatException e){
            concat.setLength(0);
            String errorMessage = getValueNotANumberErrorString(fieldName);
            throw new ValidationException(errorMessage);
        }

        checkNumberForNegativeValue(out, fieldName);

        return out;
    }

    Integer validateIntField(String field, String fieldName, boolean allowEmpty) throws ValidationException, DAOException {
        if(checkForEmptyField(field, fieldName, allowEmpty)){
            return INT_ZERO;
        }

        Integer out;

        try {
            out = Integer.parseInt(field);
        } catch (NumberFormatException e){
            concat.setLength(0);
            String errorMessage = getValueNotANumberErrorString(fieldName);
            throw new ValidationException(errorMessage);
        }

        checkNumberForNegativeValue(out, fieldName);

        return out;
    }

    Double validateDoubleField(String field, String fieldName, boolean allowEmpty) throws ValidationException, DAOException {
        if(checkForEmptyField(field, fieldName, allowEmpty)){
            return DOUBLE_ZERO;
        }

        Double out;

        try {
            out = Double.parseDouble(field);
        } catch (NumberFormatException e){
            concat.setLength(0);
            String errorMessage = getValueNotANumberErrorString(fieldName);
            throw new ValidationException(errorMessage);
        }

        checkNumberForNegativeValue(out, fieldName);

        return out;
    }

    Timestamp validateDateTimeField(String field, String fieldName, boolean allowEmpty) throws ValidationException, DAOException {
        if(checkForEmptyField(field, fieldName, allowEmpty)){
            return new Timestamp(new Date().getTime());
        }

        Timestamp out;

        try {
            out = Timestamp.valueOf(field);
        } catch (IllegalArgumentException e){
            concat.setLength(0);
            String errorMessage = concat.append(Localization.getLocalization().getString("incorrectDateTimeFormat"))
                    .append(" ").append(fieldName).toString();
            throw new ValidationException(errorMessage);
        }

        return out;
    }

    private boolean checkForEmptyField(String field, String fieldName, boolean allowEmpty) throws ValidationException, DAOException {
        if(field.isEmpty() && allowEmpty){
            return true;
        } else if (field.isEmpty()) {
            concat.setLength(0);
            String errorMessage = concat.append(Localization.getLocalization().getString("emptyNotAllowed"))
                    .append(" ").append(fieldName).toString();
            throw new ValidationException(errorMessage);
        }

        return false;
    }

    private void checkFieldLength(String field, String fieldName, int length) throws ValidationException, DAOException {
        if (field.length() > length) {
            concat.setLength(0);
            String errorMessage = concat.append(Localization.getLocalization().getString("stringLengthError"))
                    .append(" ").append(length).append(" : ").append(fieldName).toString();
            throw new ValidationException(errorMessage);
        }
    }

    private void checkNumberForNegativeValue(Number value, String fieldName) throws ValidationException, DAOException {
        if ((value instanceof Long && value.longValue() < 0)
                || (value instanceof Double && value.doubleValue() < 0)
                || (value instanceof Integer && value.intValue() < 0)) {

            concat.setLength(0);
            String errorMessage = concat.append(Localization.getLocalization().getString("negativeValueError"))
                    .append(" ").append(fieldName).toString();
            throw new ValidationException(errorMessage);
        }
    }

    private String getValueNotANumberErrorString(String fieldName) throws DAOException {
        return concat.append(Localization.getLocalization().getString("valueNotANumberError"))
                .append(" ").append(fieldName).toString();
    }
}



