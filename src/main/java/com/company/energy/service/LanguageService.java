package com.company.energy.service;

import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;
import com.company.energy.exception.ValidationException;

import java.io.IOException;
import com.company.energy.model.Language;
import com.company.energy.validator.ServiceParametersValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.util.*;
import static com.company.energy.util.Constants.*;
import static com.company.energy.util.Constants.LAST_URL;

public class LanguageService extends AbstractService {
    private static LanguageService instance;
    private List<String> allowedParameters = new ArrayList<>();
    private ResourceBundle localizationBundle = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_NAME);
    private ResourceBundle errorBundle = ResourceBundle.getBundle(DAO_ERROR_BUNDLE_NAME);

    private LanguageService() throws DAOException {
        init();
    }

    public static synchronized LanguageService getInstance() throws DAOException {
        if (instance == null) {
            instance = new LanguageService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            switchLocale(parameters, request);

            response.sendRedirect((String) request.getSession().getAttribute(LAST_URL));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void switchLocale(Map<String, String[]> parameters, HttpServletRequest request) throws DAOException {
        Long languageId = Long.parseLong(parameters.get(LANGUAGE_ID)[0]);
        Language language = languageDAO.getLanguageById(languageId).get(0);
        Locale locale = new Locale(language.getCode(), language.getCountry());
        Locale.setDefault(locale);
        localizationBundle = ResourceBundle.getBundle(LOCALIZATION_BUNDLE_NAME);
        errorBundle = ResourceBundle.getBundle(DAO_ERROR_BUNDLE_NAME);
        Config.set(request.getSession(), Config.FMT_LOCALE, locale);
    }

    public ResourceBundle getLocalization() {
        return localizationBundle;
    }

    public ResourceBundle getDAOErrorLocalization() {
        return errorBundle;
    }

    private void init() {
        allowedParameters.add(LANGUAGE_ID);
    }
}
