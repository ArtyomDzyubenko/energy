package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Language;
import com.company.energy.util.ConnectionPool;
import com.company.energy.util.Constants;
import com.company.energy.util.IConnectionPool;
import com.company.energy.util.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LanguageDAO implements AbstractLanguageDAO {
    private static final String GET_LANGUAGES =
            "select * from languages";
    private static final String GET_LANGUAGE_BY_ID =
            "select * from languages where id = ?";

    private static final IConnectionPool pool = ConnectionPool.getInstance();

    private static LanguageDAO instance;

    private LanguageDAO() {}

    public static synchronized LanguageDAO getInstance() {
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

    List<Language> getLanguages(Long id, String query) throws DAOException {
        List<Language> languages = new ArrayList<>();

        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            if (id != null) {
                preparedStatement.setLong(1, id);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                languages.add(getLanguageFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return languages;
    }

    private Language getLanguageFromResultSet(ResultSet resultSet) throws SQLException {
        Language language = new Language();
        language.setId(resultSet.getLong(Constants.ID));
        language.setName(resultSet.getString(Constants.LANGUAGE_NAME));
        language.setCode(resultSet.getString(Constants.LANGUAGE_CODE));
        language.setCountry(resultSet.getString(Constants.LANGUAGE_COUNTRY));

        return language;
    }
}
