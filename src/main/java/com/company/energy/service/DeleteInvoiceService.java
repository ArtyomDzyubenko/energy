package com.company.energy.service;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.validator.ServiceParametersValidator;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.INVOICES_URL_LAST_STATE;
import static com.company.energy.util.Constants.INVOICE_ID;

public class DeleteInvoiceService extends AbstractService {
    private static DeleteInvoiceService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private DeleteInvoiceService() throws DAOException {
        init();
    }

    public static synchronized DeleteInvoiceService getInstance() throws DAOException {
        if (instance == null) {
            instance = new DeleteInvoiceService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long invoiceId = getInvoiceId(parameters, !allowEmpty);

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

    private void init() {
        allowedParameters.add(INVOICE_ID);
    }
}