package com.epam.energy.service;

import com.epam.energy.exception.DAOException;
import com.epam.energy.exception.ServiceException;
import com.epam.energy.exception.ValidationException;
import java.io.IOException;
import com.epam.energy.model.Invoice;
import com.epam.energy.validator.ServiceParametersValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static com.epam.energy.util.Constants.*;
import static com.epam.energy.util.Constants.INVOICES_JSP;

public class GetInvoicesService extends AbstractService {
    private static GetInvoicesService instance;
    private List<String> allowedParameters = new ArrayList<>();

    private GetInvoicesService() throws DAOException{
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
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();

            parametersValidator.validate(parameters, allowedParameters);

            Long userId = getUserId(parameters, !allowEmpty);
            String authorizedUserSessionId = AuthService.getInstance().getAuthorizedUserSessionId();

            parametersValidator.validateSecretKey(userId.toString(), authorizedUserSessionId, parameters.get(SECRET_KEY)[0]);

            List<Invoice> invoices = invoiceDAO.getInvoicesByUserId(userId);

            saveLastServiceURL(INVOICES_URL_LAST_STATE, request);
            request.setAttribute(USER_ID, userId);
            request.setAttribute(INVOICES_ATTRIBUTE, invoices);
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

    private void init() {
        allowedParameters.addAll(Arrays.asList(USER_ID, SECRET_KEY));
    }
}
