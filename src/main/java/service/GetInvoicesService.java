package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.Invoice;
import validator.UserValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import static util.Constants.*;
import static util.Constants.INVOICES_JSP;
import static util.ServicesAllowedInputParametersLists.getInvoicesServiceAllowedInputParameters;

public class GetInvoicesService extends AbstractService {
    private static GetInvoicesService instance;

    private GetInvoicesService() throws DAOException{}

    public static synchronized GetInvoicesService getInstance() throws DAOException {
        if (instance==null){
            instance = new GetInvoicesService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, getInvoicesServiceAllowedInputParameters);

            Long userId = getUserId(parameters);

            String authUserSessionId = AuthService.getInstance().getAuthUserSessionId();
            parametersValidator.validateSecretKey(userId.toString(), authUserSessionId, parameters.get(SECRET_KEY)[0]);
            List<Invoice> invoices = invoiceDAO.getInvoicesByUserId(userId);

            saveLastServiceURL(INVOICES_URL_LAST_STATE, request);
            request.setAttribute(USER_ID, userId);
            request.setAttribute(INVOICES_ATTRIBUTE_NAME, invoices);
            request.getRequestDispatcher(INVOICES_JSP).forward(request, response);
        } catch (ServletException e) {
            throw new ServiceException(e);
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }
}
