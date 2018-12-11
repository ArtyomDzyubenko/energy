package DAO;

import exception.DAOException;
import model.Address;
import java.util.List;

public class AddressDAO extends AbstractAddressDAO {
    private static final String DELETE_ADDRESS = "delete from addresses where id = ?;";
    private static final String GET_ADDRESSES = "select *\n" +
            "from addresses;";
    private static final String GET_ADDRESSES_BY_USER_ID = "select *\n" +
            "from addresses a\n" +
            "left join users u on u.id = a.userId\n" +
            "where u.id = ?;";
    private static final String GET_ADDRESS_BY_ID = "select *\n" +
            "from addresses\n" +
            "where id = ?;";
    private static final String INSERT_ADDRESS_BY_USER_ID =  "insert into addresses(building, flat, streetId, userId)\n" +
            "values(?, ?, ?, ?);";
    private static final String UPDATE_ADDRESS_BY_ID = "update addresses\n" +
            "set building = ?, flat = ?, streetId = ?, userId=?\n" +
            "where id = ?;";
    private static AddressDAO instance;

    private AddressDAO() throws DAOException {}

    public static synchronized AddressDAO getInstance() throws DAOException {
        if (instance==null){
            instance = new AddressDAO();
        }

        return instance;
    }

    @Override
    public List<Address> getAddressesByUserId(Long id) throws DAOException {
        return getAddressById(id, GET_ADDRESSES_BY_USER_ID);
    }

    @Override
    public List<Address> getAddressById(Long id) throws DAOException {
        return getAddressById(id, GET_ADDRESS_BY_ID);
    }

    @Override
    public List<Address> getAll() throws DAOException {
        return getAddressById(null, GET_ADDRESSES);
    }

    @Override
    public void deleteAddressById(Long id) throws DAOException {
        deleteEntityById(id, DELETE_ADDRESS);
    }

    @Override
    public void addAddressByUserId(Address address) throws DAOException {
        addOrEditAddress(address, INSERT_ADDRESS_BY_USER_ID);
    }

    @Override
    public void editAddress(Address address) throws DAOException {
        addOrEditAddress(address, UPDATE_ADDRESS_BY_ID);
    }
}
