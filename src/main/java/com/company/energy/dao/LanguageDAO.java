package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Language;

import java.util.List;

public class LanguageDAO extends AbstractLanguageDAO {
    private static final String GET_LANGUAGES = "select * from languages;";
    private static final String GET_LANGUAGE_BY_ID = "select * from languages where id = ?;";
    private static LanguageDAO instance;

    private LanguageDAO() throws DAOException {}

    public static synchronized LanguageDAO getInstance() throws DAOException {
        if (instance == null) {
            instance = new LanguageDAO();
        }

        return instance;
    }

    @Override
    public List<Language> getAll() throws DAOException {
        return getLanguages(null, GET_LANGUAGES);
    }

    @Override
    public List<Language> getLanguageById(Long id) throws DAOException {
        return getLanguages(id, GET_LANGUAGE_BY_ID);
    }
}
