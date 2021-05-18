package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.User;
import com.company.energy.service.AuthService;
import com.company.energy.util.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements AbstractUserDAO {
    private static final String GET_USER_BY_ID =
            "select * " +
            "from users " +
            "where id = ?";
    private static final String GET_USER_BY_LOGIN_AND_PASSWORD =
            "select * " +
            "from users " +
            "where login like ? and password like ?";
    private static final String GET_USERS =
            "select * " +
            "from users " +
            "where isAdmin = false";
    private static final String INSERT_USER =
            "insert into users(login, password, firstName, lastName, phone, email, personalAccount, isAdmin) " +
            "values(?, ?, ?, ?, ?, ?, ?, false)";
    private static final String UPDATE_USER =
            "update users " +
            "set login = ?, password = ?, firstName = ?, lastName = ?, phone = ?, email = ?, personalAccount = ? " +
            "where id = ?";
    private static final String INSERT_REGISTERED_USER =
            "insert into users(login, password, phone, email, isAdmin) " +
            "values(?, ?, ?, ?, false)";
    private static final String UPDATE_REGISTERED_USER =
            "update users " +
            "set login = ?, password = ?, phone = ?, email = ? " +
            "where id = ?";
    private static final String DELETE_USER =
            "delete from users where id = ?";

    private static final IConnectionPool pool = ConnectionPool.getInstance();

    private static UserDAO instance;

    private UserDAO() { }

    public static synchronized UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }

        return instance;
    }

    @Override
    public List<User> getAll() throws DAOException {
        return getUsers(null, GET_USERS);
    }

    @Override
    public List<User> getUserById(Long id) throws DAOException {
        return getUsers(id, GET_USER_BY_ID);
    }

    public User getUserByLoginAndPassword(String login, String password) throws DAOException {
        return getUser(login, password, GET_USER_BY_LOGIN_AND_PASSWORD);
    }

    @Override
    public void addUser(User user) throws DAOException {
        addOrEditUser(user, INSERT_USER);
    }

    @Override
    public void editUser(User user) throws DAOException {
        addOrEditUser(user, UPDATE_USER);
    }

    @Override
    public void registerUser(User user) throws DAOException {
        registerUser(user, INSERT_REGISTERED_USER);
    }

    @Override
    public void updateRegisteredUser(User user) throws DAOException {
        registerUser(user, UPDATE_REGISTERED_USER);
    }

    @Override
    public void deleteUserById(Long id) throws DAOException{
        deleteEntityById(id);
    }

    private void deleteEntityById(Long id) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(DELETE_USER)) {

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    List<User> getUsers(Long id, String query) throws DAOException {
        List<User> users = new ArrayList<>();
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            if (id != null) {
                preparedStatement.setLong(1, id);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(getUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return users;
    }

    User getUser(String login, String password, String query) throws DAOException {
        User user = new User();

        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, Encryption.encrypt(password));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return user;
    }

    void addOrEditUser(User user, String query) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            setUserToPreparedStatement(user, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    void registerUser(User user, String query) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            setRegisteredUserToPreparedStatement(user, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException, DAOException {
        User user = new User();
        Long id = resultSet.getLong(Constants.ID);
        user.setId(id);
        user.setLogin(resultSet.getString(Constants.USER_LOGIN));
        user.setPassword(Encryption.decrypt(resultSet.getString(Constants.USER_PASSWORD)));
        user.setFirstName(resultSet.getString(Constants.USER_FIRST_NAME));
        user.setLastName(resultSet.getString(Constants.USER_LAST_NAME));
        user.setPhone(resultSet.getLong(Constants.USER_PHONE));
        user.setEmail(resultSet.getString(Constants.USER_EMAIL));
        user.setPersonalAccount(resultSet.getInt(Constants.USER_PERSONAL_ACCOUNT));
        user.setAdmin(resultSet.getBoolean(Constants.USER_IS_ADMIN));

        return user;
    }

    private void setUserToPreparedStatement(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, Encryption.encrypt(user.getPassword()));
        preparedStatement.setString(3, user.getFirstName());

        if (!user.getLastName().equals(Constants.EMPTY_STRING)) {
            preparedStatement.setString(4, user.getLastName());
        } else {
            preparedStatement.setNull(4, Types.VARCHAR);
        }

        if (!user.getPhone().equals(Constants.LONG_ZERO)) {
            preparedStatement.setLong(5, user.getPhone());
        } else {
            preparedStatement.setNull(5, Types.BIGINT);
        }

        if (!user.getEmail().equals(Constants.EMPTY_STRING)) {
            preparedStatement.setString(6, user.getEmail());
        } else {
            preparedStatement.setNull(6, Types.VARCHAR);
        }

        if (!user.getPersonalAccount().equals(0)) {
            preparedStatement.setInt(7, user.getPersonalAccount());
        } else {
            preparedStatement.setNull(7, Types.INTEGER);
        }

        if (!user.getId().equals(Constants.LONG_ZERO)) {
            preparedStatement.setLong(8, user.getId());
        }
    }

    private void setRegisteredUserToPreparedStatement(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, Encryption.encrypt(user.getPassword()));

        if (!user.getPhone().equals(Constants.LONG_ZERO)) {
            preparedStatement.setLong(3, user.getPhone());
        } else {
            preparedStatement.setNull(3, Types.BIGINT);
        }

        if (!user.getEmail().equals(Constants.EMPTY_STRING)) {
            preparedStatement.setString(4, user.getEmail());
        } else {
            preparedStatement.setNull(4, Types.VARCHAR);
        }

        if (!user.getId().equals(Constants.LONG_ZERO)) {
            preparedStatement.setLong(5, user.getId());
        }
    }
}
