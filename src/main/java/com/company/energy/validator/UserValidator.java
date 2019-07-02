package com.company.energy.validator;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ValidationException;
import com.company.energy.util.Constants;

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

        return validateStringField(login, Constants.STRING_MAX_LENGTH, Constants.USER_LOGIN_REGEX, fieldName, allowEmpty);
    }

    public String validatePassword(String password, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("password");

        return validateStringField(password, Constants.STRING_MAX_LENGTH, fieldName, allowEmpty);
    }

    public String validateFirstName(String firstName, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("firstName");

        return validateStringField(firstName, Constants.STRING_MAX_LENGTH, Constants.USER_FIRST_LAST_NAME_REGEX, fieldName, allowEmpty);
    }

    public String validateLastName(String lastName, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("lastName");

        return validateStringField(lastName, Constants.STRING_MAX_LENGTH, Constants.USER_FIRST_LAST_NAME_REGEX, fieldName, allowEmpty);
    }

    public Long validatePhone(String phone, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("phone");
        Long out = validateLongField(phone, fieldName, allowEmpty);

        if (out < Constants.MIN_PHONE_NUMBER || out > Constants.MAX_PHONE_NUMBER ) {
            concat.setLength(0);
            String errorMessage = concat.append(getErrorLocalization("outOfRange")).append(" ")
                    .append(Constants.MIN_PHONE_NUMBER).append(" - ").append(Constants.MAX_PHONE_NUMBER).append(" : ")
                    .append(getErrorLocalization("phone")).toString();

            throw new ValidationException(errorMessage);
        }

        return out;
    }

    public String validateEmail(String email, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("email");

        return validateStringField(email, Constants.STRING_MAX_LENGTH, Constants.EMAIL_REGEX, fieldName, allowEmpty);
    }

    public Integer validatePersonalAccount(String personalAccount, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("personalAccount");

        return validateIntField(personalAccount, fieldName, allowEmpty);
    }
}
