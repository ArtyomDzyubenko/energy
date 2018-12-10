package service;

import exception.DAOException;
import exception.ServiceException;
import java.io.IOException;
import java.util.List;
import model.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static util.Constants.*;
import static util.Constants.RESOURCES_JSP;

public class GetResourcesService extends AbstractService {
    private static GetResourcesService instance;

    private GetResourcesService() throws DAOException {};

    public static synchronized GetResourcesService getInstance() throws DAOException {
        if (instance==null){
            instance = new GetResourcesService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            List<Resource> resources = resourceDAO.getAll();

            saveLastServiceURL(RESOURCES_URL_LAST_STATE, request);
            request.setAttribute(RESOURCES_ATTRIBUTE_NAME, resources);
            request.getRequestDispatcher(RESOURCES_JSP).forward(request, response);
        } catch (ServletException e) {
            throw new ServiceException(e);
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
