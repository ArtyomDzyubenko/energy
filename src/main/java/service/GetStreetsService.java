package service;

import exception.DAOException;
import exception.ServiceException;
import java.io.IOException;
import java.util.List;
import model.Street;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static util.Constants.*;
import static util.Constants.STREETS_JSP;

public class GetStreetsService extends AbstractService {
    private static GetStreetsService instance;

    private GetStreetsService() throws DAOException {}

    public static synchronized GetStreetsService getInstance() throws DAOException {
        if (instance==null){
            instance = new GetStreetsService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            List<Street> streets = streetDAO.getAll();

            saveLastServiceURL(STREETS_URL_LAST_STATE, request);
            request.setAttribute(STREETS_ATTRIBUTE_NAME, streets);
            request.getRequestDispatcher(STREETS_JSP).forward(request, response);
        } catch (ServletException e) {
            throw new ServiceException(e);
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
