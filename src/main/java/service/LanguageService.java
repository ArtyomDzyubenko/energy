package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.Language;
import validator.ServiceParametersValidator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import static util.Constants.*;
import static util.Constants.LAST_URL;

public class LanguageService extends AbstractService {
    private static LanguageService instance;
    private List<String> allowedParameters = new ArrayList<>();
    private ResourceBundle localizationBundle = ResourceBundle.getBundle(localizationBundleName);
    private ResourceBundle errorBundle = ResourceBundle.getBundle(errorBundleName);

    private LanguageService() throws DAOException {
        init();
    }

    public static synchronized LanguageService getInstance() throws DAOException {
        if (instance == null) {
            instance = new LanguageService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            ServiceParametersValidator parametersValidator = ServiceParametersValidator.getInstance();

            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, allowedParameters);

            Long languageId = Long.parseLong(request.getParameter(LANGUAGE_ID));
            Language language = languageDAO.getLanguageById(languageId).get(0);
            Locale locale = new Locale(language.getCode(), language.getCountry());
            switchLocale(locale);

            response.sendRedirect((String) request.getSession().getAttribute(LAST_URL));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void switchLocale(Locale locale){
        Locale.setDefault(locale);
        localizationBundle = ResourceBundle.getBundle(localizationBundleName);
        errorBundle = ResourceBundle.getBundle(errorBundleName);
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
