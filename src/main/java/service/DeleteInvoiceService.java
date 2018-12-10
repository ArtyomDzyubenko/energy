package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import validator.InvoiceValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import static util.Constants.INVOICES_URL_LAST_STATE;
import static util.Constants.INVOICE_ID;
import static util.ServicesAllowedInputParametersLists.deleteInvoiceServiceAllowedInputParameters;

public class DeleteInvoiceService extends AbstractService {
    private static DeleteInvoiceService instance;

    private DeleteInvoiceService() throws DAOException{}

    public static synchronized DeleteInvoiceService getInstance() throws DAOException {
        if(instance==null){
            instance = new DeleteInvoiceService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, deleteInvoiceServiceAllowedInputParameters);

            Long invoiceId = getInvoiceId(parameters);

            invoiceDAO.deleteInvoiceById(invoiceId);

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
