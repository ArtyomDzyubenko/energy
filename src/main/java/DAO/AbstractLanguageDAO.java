package DAO;

import exception.DAOException;
import model.Language;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static util.Constants.*;
import static util.Constants.LANGUAGE_COUNTRY;

public abstract class AbstractLanguageDAO extends AbstractDAO {
    AbstractLanguageDAO() throws DAOException {}

    public abstract List<Language> getAll() throws DAOException;
    public abstract List<Language> getLanguageById(Long id) throws DAOException;

    List<Language> getLanguages(Long id, String query) throws DAOException {
        List<Language> languages = new ArrayList<>();

        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if(id != null){
                preparedStatement.setLong(1, id);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Language language = new Language();
                language.setId(resultSet.getLong(ID));
                language.setName(resultSet.getString(LANGUAGE_NAME));
                language.setCode(resultSet.getString(LANGUAGE_CODE));
                language.setCountry(resultSet.getString(LANGUAGE_COUNTRY));
                languages.add(language);
            }
        } catch (SQLException e){
            throw new DAOException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return languages;
    }
}
