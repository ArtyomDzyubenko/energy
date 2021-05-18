package com.company.energy.service;

import com.company.energy.dao.AbstractInvoiceDAO;
import com.company.energy.dao.InvoiceDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;

import java.io.IOException;
import com.company.energy.model.Invoice;
import com.company.energy.util.Encryption;
import com.company.energy.validator.ServiceParametersValidator;
import com.company.energy.validator.UserValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.company.energy.util.Constants.*;
import static com.company.energy.util.Constants.INVOICES_JSP;

public class GetInvoicesService extends AbstractService {
    private static final List<String> allowedParameters = new ArrayList<>();

    private static final AbstractInvoiceDAO invoiceDAO = InvoiceDAO.getInstance();
    private static final UserValidator userValidator = UserValidator.getInstance();
    private static final ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

    private static GetInvoicesService instance;

    private GetInvoicesService() throws DAOException {
        init();
    }

    public static synchronized GetInvoicesService getInstance() throws DAOException {
        if (instance == null) {
            instance = new GetInvoicesService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();

            parametersValidator.validate(parameters, allowedParameters);

            Long userId = userValidator.validateAndGetId(parameters.get(USER_ID)[0], !allowEmpty);

            parametersValidator.validateSecretKey(userId.toString(), request.getSession().getId(), parameters.get(SECRET_KEY)[0]);

            List<Invoice> invoices = invoiceDAO.getInvoicesByUserId(userId);
            invoices.forEach(invoice -> invoice.setSecretKey(Encryption.encrypt(invoice.getId() + request.getSession().getId())));

            saveLastServiceURL(INVOICES_URL_LAST_STATE, request);
            request.setAttribute(USER_ID, userId);
            request.setAttribute(INVOICES_ATTRIBUTE, invoices);
            request.getRequestDispatcher(INVOICES_JSP).forward(request, response);
        } catch (ServletException | IOException | DAOException | ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void init() {
        allowedParameters.addAll(Arrays.asList(USER_ID, SECRET_KEY));
    }
}
