package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static util.Constants.USERS_URL_LAST_STATE;
import static util.ServicesAllowedInputParametersLists.deleteUserServiceAllowedInputParameters;

public class DeleteUserService extends AbstractService {
    private static DeleteUserService instance;

    private DeleteUserService() throws DAOException{}

    public static synchronized DeleteUserService getInstance() throws DAOException {
        if (instance==null) {
            instance = new DeleteUserService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, deleteUserServiceAllowedInputParameters);

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
}
