package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Street;
import com.company.energy.util.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.company.energy.util.Constants.*;

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
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            if (id != null) {
                preparedStatement.setLong(1, id);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                streets.add(getStreetFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return streets;
    }

    void addOrEditStreet(Street street, String query) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            setStreetToPreparedStatement(street, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.getExceptionMessage(e);
        }
    }

    private Street getStreetFromResultSet(ResultSet resultSet) throws SQLException {
        Street street = new Street();
        street.setId(resultSet.getLong(ID));
        street.setName(resultSet.getString(STREET_NAME));

        return street;
    }

    private void setStreetToPreparedStatement(Street street, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, street.getName());

        if (!street.getId().equals(LONG_ZERO)) {
            preparedStatement.setLong(2, street.getId());
        }
    }
}
