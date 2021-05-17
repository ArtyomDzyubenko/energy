package com.company.energy.service;

import com.company.energy.dao.AbstractInvoiceDAO;
import com.company.energy.dao.InvoiceDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;
import com.company.energy.validator.InvoiceValidator;
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
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractInvoiceDAO invoiceDAO = InvoiceDAO.getInstance();
    private static final InvoiceValidator invoiceValidator = InvoiceValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();


    private static DeleteInvoiceService instance;

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
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long invoiceId = invoiceValidator.validateAndGetId(parameters.get(INVOICE_ID)[0], !allowEmpty);

            invoiceDAO.deleteInvoiceById(invoiceId);

            response.sendRedirect(getLastServiceURL(INVOICES_URL_LAST_STATE, request));
        } catch (IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.add(INVOICE_ID);
    }
}
