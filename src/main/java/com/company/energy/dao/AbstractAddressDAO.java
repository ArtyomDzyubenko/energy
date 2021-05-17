package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Address;
import com.company.energy.model.Street;
import com.company.energy.service.AuthService;
import com.company.energy.util.Encryption;
import com.company.energy.util.PooledConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static com.company.energy.util.Constants.*;

public abstract class AbstractAddressDAO extends AbstractDAO {
    private AbstractStreetDAO streetDAO = StreetDAO.getInstance();

    AbstractAddressDAO() throws DAOException { }

    public abstract List<Address> getAddressesByUserId(Long userId) throws DAOException;
    public abstract List<Address> getAddressById(Long addressId) throws DAOException;
    public abstract List<Address> getAll() throws DAOException;
    public abstract void addAddressByUserId(Address address) throws DAOException;
    public abstract void editAddress(Address address) throws DAOException;
    public abstract void deleteAddressById(Long addressId) throws DAOException;

    List<Address> getAddresses(Long id, String query) throws DAOException {
        List<Address> addresses = new ArrayList<>();

        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            if (id != null) {
                preparedStatement.setLong(1, id);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                addresses.add(getAddressFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return addresses;
    }

    void addOrEditAddress(Address address, String query) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            setAddressToPreparedStatement(address, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.getExceptionMessage(e);
        }
    }

    private Address getAddressFromResultSet(ResultSet resultSet) throws SQLException, DAOException {
        Address address = new Address();
        Long addressId = resultSet.getLong(ID);
        address.setId(addressId);
        address.setBuilding(resultSet.getString(ADDRESS_BUILDING));
        address.setFlat(resultSet.getString(ADDRESS_FLAT));
        List<Street> streets = streetDAO.getStreetByAddressId(addressId);

        if (!streets.isEmpty()) {
            address.setStreet(streets.get(0));
        } else {
            address.setStreet(new Street());
        }

        address.setUserId(resultSet.getLong(USER_ID));
        String authorizedUserSessionId = AuthService.getInstance().getAuthorizedUserSessionId();
        address.setSecretKey(Encryption.encrypt(addressId.toString() + authorizedUserSessionId));

        return address;
    }

    private void setAddressToPreparedStatement(Address address, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, address.getBuilding());

        if (!address.getFlat().equals(EMPTY_STRING)) {
            preparedStatement.setString(2, address.getFlat());
        } else{
            preparedStatement.setNull(2, Types.VARCHAR);
        }

        preparedStatement.setLong(3, address.getStreet().getId());

        if (address.getId().equals(LONG_ZERO)) {
            preparedStatement.setLong(4, address.getUserId());
        } else {
            preparedStatement.setLong(4, address.getUserId());
            preparedStatement.setLong(5, address.getId());
        }
    }
}