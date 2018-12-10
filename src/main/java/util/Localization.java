package util;

import exception.DAOException;
import service.SwitchLanguageService;
import java.util.ResourceBundle;

public final class Localization {
    public static ResourceBundle getLocalization() throws DAOException {
        return SwitchLanguageService.getInstance().getLocalizationBundle();
    }

    public static ResourceBundle getDAOErrorLocalization() throws DAOException {
        return SwitchLanguageService.getInstance().getErrorBundle();
    }
}
