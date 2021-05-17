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

    public Long validateAndGetId(String id, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("userId");
        
        return validateAndGetLongField(id, fieldName, allowEmpty);
    }

    public String validateAndGetLogin(String login, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("login");

        return validateAndGetStringField(login, Constants.STRING_MAX_LENGTH, Constants.USER_LOGIN_REGEX, fieldName, allowEmpty);
    }

    public String validateAndGetPassword(String password, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("password");

        return validateAndGetStringField(password, Constants.STRING_MAX_LENGTH, fieldName, allowEmpty);
    }

    public String validateAndGetFirstName(String firstName, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("firstName");

        return validateAndGetStringField(firstName, Constants.STRING_MAX_LENGTH, Constants.USER_FIRST_LAST_NAME_REGEX, fieldName, allowEmpty);
    }

    public String validateAndGetLastName(String lastName, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("lastName");

        return validateAndGetStringField(lastName, Constants.STRING_MAX_LENGTH, Constants.USER_FIRST_LAST_NAME_REGEX, fieldName, allowEmpty);
    }

    public Long validateAndGetPhone(String phone, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("phone");
        Long out = validateAndGetLongField(phone, fieldName, allowEmpty);

        if (out < Constants.MIN_PHONE_NUMBER || out > Constants.MAX_PHONE_NUMBER ) {
            String errorMessage =
                    getErrorLocalization("outOfRange") + " " +
                    Constants.MIN_PHONE_NUMBER + " - " + Constants.MAX_PHONE_NUMBER + " : " +
                    getErrorLocalization("phone");

            throw new ValidationException(errorMessage);
        }

        return out;
    }

    public String validateAndGetEmail(String email, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("email");

        return validateAndGetStringField(email, Constants.STRING_MAX_LENGTH, Constants.EMAIL_REGEX, fieldName, allowEmpty);
    }

    public Integer validateAndGetPersonalAccount(String personalAccount, boolean allowEmpty) throws ValidationException, DAOException {
        String fieldName = getErrorLocalization("personalAccount");

        return validateAndGetIntField(personalAccount, fieldName, allowEmpty);
    }
}
