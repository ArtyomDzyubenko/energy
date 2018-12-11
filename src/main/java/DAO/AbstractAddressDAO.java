package DAO;

import exception.DAOException;
import model.Address;
import model.Street;
import service.AuthService;
import util.Encryption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static util.Constants.*;

public abstract class AbstractAddressDAO extends AbstractDAO {
    private AbstractStreetDAO streetDAO = StreetDAO.getInstance();

    AbstractAddressDAO() throws DAOException { }

    public abstract List<Address> getAddressesByUserId(Long userId) throws DAOException;
    public abstract List<Address> getAddressById(Long addressId) throws DAOException;
    public abstract List<Address> getAll() throws DAOException;
    public abstract void addAddressByUserId(Address address) throws DAOException;
    public abstract void editAddress(Address address) throws DAOException;
    public abstract void deleteAddressById(Long addressId) throws DAOException;

    List<Address> getAddressById(Long id, String query) throws DAOException {
        List<Address> addresses = new ArrayList<>();
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            if (id != null) {
                preparedStatement.setLong(1, id);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
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
                String authUserSessionId = AuthService.getInstance().getAuthUserSessionId();
                address.setSecretKey(Encryption.encrypt(addressId.toString() + authUserSessionId));
                addresses.add(address);
            }
        } catch (SQLException e){
            throw new DAOException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return addresses;
    }

    void addOrEditAddress(Address address, String query) throws DAOException {
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, address.getBuilding());

            if(!address.getFlat().equals(EMPTY_STRING)){
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

            preparedStatement.executeUpdate();
        } catch (SQLException e){
            exceptionHandler.getExceptionMessage(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }
}
