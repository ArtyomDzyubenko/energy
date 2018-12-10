package DAO;

import exception.DAOException;
import model.Language;
import java.util.List;
import static util.Constants.LONG_ZERO;

public class LanguageDAO extends AbstractLanguageDAO {
    private static final String GET_LANGUAGES = "select * from languages;";
    private static final String GET_LANGUAGE_BY_ID = "select * from languages where id = ?;";
    private static LanguageDAO instance;

    private LanguageDAO() throws DAOException {}

    public static synchronized LanguageDAO getInstance() throws DAOException {
        if (instance==null){
            instance = new LanguageDAO();
        }

        return instance;
    }

    @Override
    public List<Language> getAll() throws DAOException {
        return getLanguages(LONG_ZERO, GET_LANGUAGES);
    }

    public List<Language> getLanguageById(Long id) throws DAOException {
        return getLanguages(id, GET_LANGUAGE_BY_ID);
    }
}
