package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import validator.ServiceParametersValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static util.Constants.INVOICES_URL_LAST_STATE;
import static util.Constants.INVOICE_ID;

public class PayInvoiceService extends AbstractService {
    private static PayInvoiceService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private PayInvoiceService() throws DAOException{
        init();
    }

    public static synchronized PayInvoiceService getInstance() throws DAOException {
        if(instance==null){
            instance = new PayInvoiceService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

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

    private void init() {
        allowedParameters.add(INVOICE_ID); 
    }
}
