package com.epam.energy.dao;

import com.epam.energy.exception.DAOException;
import com.epam.energy.model.Street;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static com.epam.energy.util.Constants.*;
import static com.epam.energy.util.Constants.LONG_ZERO;

public abstract class AbstractStreetDAO extends AbstractDAO {
    AbstractStreetDAO() throws DAOException { }

    public abstract List<Street> getAll() throws DAOException;
    public abstract List<Street> getStreetByAddressId(Long id) throws DAOException;
    public abstract List<Street> getStreetById(Long id) throws DAOException;
    public abstract void addStreet(Street street) throws DAOException;
    public abstract void editStreet(Street street) throws DAOException;
    public abstract void deleteStreetById(Long id) throws DAOException;

    List<Street> getStreets(Long id, String query) throws DAOException {
        List<Street> streets = new ArrayList<>();
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (id != null) {
                preparedStatement.setLong(1, id);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Street street = new Street();
                street.setId(resultSet.getLong(ID));
                street.setName(resultSet.getString(STREET_NAME));
                streets.add(street);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return streets;
    }

    void addOrEditStreet(Street street, String query) throws DAOException {
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, street.getName());

            if (!street.getId().equals(LONG_ZERO)) {
                preparedStatement.setLong(2, street.getId());
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.getExceptionMessage(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }
}
