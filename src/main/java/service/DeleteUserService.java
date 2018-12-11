package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import validator.ServiceParametersValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static util.Constants.USERS_URL_LAST_STATE;
import static util.Constants.USER_ID;

public class DeleteUserService extends AbstractService {
    private static DeleteUserService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private DeleteUserService() throws DAOException{
        init();
    }

    public static synchronized DeleteUserService getInstance() throws DAOException {
        if (instance==null) {
            instance = new DeleteUserService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long userId = getUserId(parameters);

            userDAO.deleteUserById(userId);

            response.sendRedirect(getLastServiceURL(USERS_URL_LAST_STATE, request));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.add(USER_ID);
    }
}
