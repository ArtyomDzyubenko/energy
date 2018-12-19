package com.epam.energy.validator;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ValidationException;
import static com.epam.energy.util.Constants.*;

public class UserValidator extends Validator {
    private static UserValidator instance;

    private UserValidator() {}

    public static synchronized UserValidator getInstance() {
        if (instance == null) {
            instance = new UserValidator();
        }

        return instance;
    }

    public Long validateId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("userId");
        
        return validateLongField(id, fieldName, allowEmpty);
    }

    public String validateLogin(String login, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("login");

        return validateStringField(login, STRING_MAX_LENGTH, USER_LOGIN_REGEX, fieldName, allowEmpty);
    }

    public String validatePassword(String password, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("password");

        return validateStringField(password, STRING_MAX_LENGTH, fieldName, allowEmpty);
    }

    public String validateFirstName(String firstName, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("firstName");

        return validateStringField(firstName, STRING_MAX_LENGTH, USER_FIRST_LAST_NAME_REGEX, fieldName, allowEmpty);
    }

    public String validateLastName(String lastName, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("lastName");

        return validateStringField(lastName, STRING_MAX_LENGTH, USER_FIRST_LAST_NAME_REGEX, fieldName, allowEmpty);
    }

    public Long validatePhone(String phone, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("phone");
        Long out = validateLongField(phone, fieldName, allowEmpty);

        if (out < MIN_PHONE_NUMBER || out > MAX_PHONE_NUMBER ) {
            concat.setLength(0);
            String errorMessage = concat.append(getErrorLocalization("outOfRange")).append(" ")
                    .append(MIN_PHONE_NUMBER).append(" - ").append(MAX_PHONE_NUMBER).append(" : ")
                    .append(getErrorLocalization("phone")).toString();

            throw new ValidationException(errorMessage);
        }

        return out;
    }

    public String validateEmail(String email, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("email");

        return validateStringField(email, STRING_MAX_LENGTH, EMAIL_REGEX, fieldName, allowEmpty);
    }

    public Integer validatePersonalAccount(String personalAccount, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("personalAccount");

        return validateIntField(personalAccount, fieldName, allowEmpty);
    }
}
