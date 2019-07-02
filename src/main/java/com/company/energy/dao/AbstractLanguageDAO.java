package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Language;
import com.company.energy.util.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLanguageDAO extends AbstractDAO {
    AbstractLanguageDAO() throws DAOException {}

    public abstract List<Language> getAll() throws DAOException;
    public abstract List<Language> getLanguageById(Long id) throws DAOException;

    List<Language> getLanguages(Long id, String query) throws DAOException {
        List<Language> languages = new ArrayList<>();
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (id != null) {
                preparedStatement.setLong(1, id);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                languages.add(getLanguageFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            pool.releaseConnection(connection);
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
