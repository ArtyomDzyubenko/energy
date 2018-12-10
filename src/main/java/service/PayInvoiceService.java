package service;

import DAO.InvoiceDAO;
import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import static util.Constants.INVOICES_URL_LAST_STATE;
import static util.ServicesAllowedInputParametersLists.payInvoiceServiceAllowedInputParameters;

public class PayInvoiceService extends AbstractService {
    private static PayInvoiceService instance;

    private PayInvoiceService() throws DAOException{}

    public static synchronized PayInvoiceService getInstance() throws DAOException {
        if(instance==null){
            instance = new PayInvoiceService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            InvoiceDAO invoiceDAO = InvoiceDAO.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, payInvoiceServiceAllowedInputParameters);

            Long invoiceId = getInvoiceId(parameters);
            invoiceDAO.updatePayStatusById(invoiceId, true);

            response.sendRedirect(getLastServiceURL(INVOICES_URL_LAST_STATE, request));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }
}
