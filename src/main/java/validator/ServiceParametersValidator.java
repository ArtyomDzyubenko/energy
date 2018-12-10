package validator;

import exception.DAOException;
import exception.ValidationException;
import util.Encryption;
import util.Localization;
import java.util.List;
import java.util.Map;

public class ServiceParametersValidator {
    private StringBuilder concat = new StringBuilder();
    private static ServiceParametersValidator instance;

    private ServiceParametersValidator() { }

    public static synchronized ServiceParametersValidator getInstance() {
        if(instance==null){
            instance = new ServiceParametersValidator();
        }

        return instance;
    }

    public void validate(Map<String, String[]> inputParameters, List<String> validParameters) throws ValidationException, DAOException {
        for(String parameter: validParameters){
            if(!inputParameters.containsKey(parameter)){
                concat.setLength(0);
                String errorMessage = concat.append(Localization.getLocalization().getString("parameterNotFound"))
                        .append(" ").append(parameter).toString();
                throw new ValidationException(errorMessage);
            }
        }
    }

    public void validateSecretKey(String parameter, String sessionId, String secretKey) throws DAOException {
        String encryptedPair = Encryption.encrypt(parameter + sessionId);

        if(!secretKey.equals(encryptedPair)){
            String errorMessage = Localization.getLocalization().getString("accessDenied");
            throw new SecurityException(errorMessage);
        }
    }
}
