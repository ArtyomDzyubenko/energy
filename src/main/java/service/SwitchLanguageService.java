package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import java.io.IOException;
import model.Language;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import static util.Constants.*;
import static util.Constants.LAST_URL;
import static util.ServicesAllowedInputParametersLists.switchLanguageServiceAllowedInputParameters;

public class SwitchLanguageService extends AbstractService {
    private ResourceBundle localizationBundle = ResourceBundle.getBundle(localizationBundleName);
    private ResourceBundle errorBundle = ResourceBundle.getBundle(errorBundleName);

    private static SwitchLanguageService instance;

    private SwitchLanguageService() throws DAOException {}

    public static synchronized SwitchLanguageService getInstance() throws DAOException {
        if (instance==null){
            instance = new SwitchLanguageService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, switchLanguageServiceAllowedInputParameters);

            Long languageId = Long.parseLong(request.getParameter(LANGUAGE_ID));
            Language language = languageDAO.getLanguageById(languageId).get(0);
            Locale locale = new Locale(language.getCode(), language.getCountry());
            switchLocale(locale, request);

            response.sendRedirect((String) request.getSession().getAttribute(LAST_URL));
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }

    private void switchLocale(Locale locale, HttpServletRequest request){
        Locale.setDefault(locale);
        localizationBundle = ResourceBundle.getBundle(localizationBundleName);
        errorBundle = ResourceBundle.getBundle(errorBundleName);
        Config.set(request.getSession(), Config.FMT_LOCALE, locale);
    }

    public ResourceBundle getLocalizationBundle() {
        return localizationBundle;
    }

    public ResourceBundle getErrorBundle() {
        return errorBundle;
    }
}
