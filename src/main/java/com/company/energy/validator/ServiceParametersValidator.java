package com.company.energy.validator;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ValidationException;
import com.company.energy.util.Encryption;

import java.util.List;
import java.util.Map;

public class ServiceParametersValidator extends Validator {
    private static ServiceParametersValidator instance;

    private ServiceParametersValidator() {}

    public static synchronized ServiceParametersValidator getInstance() {
        if (instance == null) {
            instance = new ServiceParametersValidator();
        }

        return instance;
    }

    public void validate(Map<String, String[]> inputParameters, List<String> validParameters) throws ValidationException, DAOException {
        for(String parameter: validParameters) {
            if (!inputParameters.containsKey(parameter)) {
                String errorMessage = getErrorLocalization("parameterNotFound") + " " + parameter;

                throw new ValidationException(errorMessage);
            }
        }
    }

    public void validateSecretKey(String parameter, String sessionId, String secretKey) throws DAOException {
        String encryptedPair = Encryption.encrypt(parameter + sessionId);

        if (!secretKey.equals(encryptedPair)) {
            String errorMessage = getErrorLocalization("accessDenied");

            throw new SecurityException(errorMessage);
        }
    }
}
