package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Address;
import com.company.energy.model.Street;
import com.company.energy.service.AuthService;
import com.company.energy.util.ConnectionPool;
import com.company.energy.util.Encryption;
import com.company.energy.util.IConnectionPool;
import com.company.energy.util.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import static com.company.energy.util.Constants.*;

public class AddressDAO implements AbstractAddressDAO {
    private static final String DELETE_ADDRESS =
            "delete from addresses where id = ?";
    private static final String GET_ADDRESSES =
            "select * " +
            "from addresses";
    private static final String GET_ADDRESSES_BY_USER_ID =
            "select * " +
            "from addresses a " +
            "left join users u on u.id = a.userId " +
            "where u.id = ?";
    private static final String GET_ADDRESS_BY_ID =
            "select * " +
            "from addresses " +
            "where id = ?";
    private static final String INSERT_ADDRESS_BY_USER_ID =
            "insert into addresses(building, flat, streetId, userId) " +
            "values(?, ?, ?, ?) ";
    private static final String UPDATE_ADDRESS_BY_ID =
            "update addresses " +
            "set building = ?, flat = ?, streetId = ?, userId = ? " +
            "where id = ?";

    private static final IConnectionPool pool = ConnectionPool.getInstance();
    private static final AbstractStreetDAO streetDAO = StreetDAO.getInstance();

    private static AddressDAO instance;

    private AddressDAO() {}

    public static synchronized AddressDAO getInstance() {
        if (instance == null) {
            instance = new AddressDAO();
        }

        return instance;
    }

    @Override
    public List<Address> getAll() throws DAOException {
        return getAddresses(null, GET_ADDRESSES);
    }

    @Override
    public List<Address> getAddressesByUserId(Long id) throws DAOException {
        return getAddresses(id, GET_ADDRESSES_BY_USER_ID);
    }

    @Override
    public List<Address> getAddressById(Long id) throws DAOException {
        return getAddresses(id, GET_ADDRESS_BY_ID);
    }

    @Override
    public void deleteAddressById(Long id) throws DAOException {
        deleteEntityById(id);
    }

    @Override
    public void addAddressByUserId(Address address) throws DAOException {
        addOrEditAddress(address, INSERT_ADDRESS_BY_USER_ID);
    }

    @Override
    public void editAddress(Address address) throws DAOException {
        addOrEditAddress(address, UPDATE_ADDRESS_BY_ID);
    }

    private void deleteEntityById(Long id) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(DELETE_ADDRESS)) {

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

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
            throw new DAOException(e);
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
