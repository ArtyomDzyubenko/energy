package com.company.energy.service;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.validator.ServiceParametersValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.INVOICES_URL_LAST_STATE;
import static com.company.energy.util.Constants.INVOICE_ID;

public class PayInvoiceService extends AbstractService {
    private static PayInvoiceService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private PayInvoiceService() throws DAOException {
        init();
    }

    public static synchronized PayInvoiceService getInstance() throws DAOException {
        if (instance == null) {
            instance = new PayInvoiceService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            boolean paid = true;

            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();

            parametersValidator.validate(parameters, allowedParameters);

            Long invoiceId = getInvoiceId(parameters, !allowEmpty);

            invoiceDAO.updatePayStatusById(invoiceId, paid);

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
