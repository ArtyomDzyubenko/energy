package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Street;
import com.company.energy.util.ConnectionPool;
import com.company.energy.util.IConnectionPool;
import com.company.energy.util.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.company.energy.util.Constants.*;

public class StreetDAO implements AbstractStreetDAO {
    private static final String GET_STREET_BY_ADDRESS_ID =
            "select * " +
            "from streets s " +
            "left join addresses a on a.streetId = s.id " +
            "where a.id = ?";
    private static final String GET_STREETS =
            "select * " +
            "from streets";
    private static final String GET_STREET_BY_ID =
            "select * from streets where id=?";
    private static final String INSERT_STREET =
            "insert into streets(name) " +
            "values(?)";
    private static final String UPDATE_STREET =
            "update streets " +
            "set name = ? " +
            "where id = ?";
    private static final String DELETE_STREET =
            "delete from streets where id = ?";

    private static final IConnectionPool pool = ConnectionPool.getInstance();

    private static StreetDAO instance;

    private StreetDAO() {}

    public static synchronized StreetDAO getInstance() {
        if (instance == null) {
            instance = new StreetDAO();
        }

        return instance;
    }

    @Override
    public List<Street> getAll() throws DAOException {
        return getStreets(null, GET_STREETS);
    }

    @Override
    public List<Street> getStreetByAddressId(Long id) throws DAOException {
        return getStreets(id, GET_STREET_BY_ADDRESS_ID);
    }

    @Override
    public List<Street> getStreetById(Long id) throws DAOException {
        return getStreets(id, GET_STREET_BY_ID);
    }

    @Override
    public void addStreet(Street street) throws DAOException {
        addOrEditStreet(street, INSERT_STREET);
    }

    @Override
    public void editStreet(Street street) throws DAOException {
        addOrEditStreet(street, UPDATE_STREET);
    }

    @Override
    public void deleteStreetById(Long id) throws DAOException {
        deleteEntityById(id);
    }

    private void deleteEntityById(Long id) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(DELETE_STREET)) {

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

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
            throw new DAOException(e);
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
