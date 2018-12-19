package com.epam.energy.dao;

import com.epam.energy.exception.DAOException;
import com.epam.energy.model.User;
import com.epam.energy.service.AuthService;
import com.epam.energy.util.Encryption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static com.epam.energy.util.Constants.*;

public abstract class AbstractUserDAO extends AbstractDAO {
    AbstractUserDAO() throws DAOException {}

    public abstract List<User> getAll() throws DAOException;
    public abstract List<User> getUserById(Long id) throws DAOException;
    public abstract User getUserByLoginAndPassword(String login, String password) throws DAOException;
    public abstract void addUser(User user) throws DAOException;
    public abstract void editUser(User user) throws DAOException;
    public abstract void registerUser(User user) throws DAOException;
    public abstract void updateRegisteredUser(User user) throws DAOException;
    public abstract void deleteUserById(Long id) throws DAOException;

    List<User> getUsers(Long id, String query) throws DAOException {
        List<User> users = new ArrayList<>();
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (id != null) {
                preparedStatement.setLong(1, id);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(getUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return users;
    }

    User getUser(String login, String password, String query) throws DAOException {
        User user = new User();

        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, Encryption.encrypt(password));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return user;
    }

    void addOrEditUser(User user, String query) throws DAOException {
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setUserToPreparedStatement(user, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.getExceptionMessage(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    void registerUser(User user, String query) throws DAOException {
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setRegisteredUserToPreparedStatement(user, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.getExceptionMessage(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException, DAOException {
        User user = new User();
        Long id = resultSet.getLong(ID);
        user.setId(id);
        user.setLogin(resultSet.getString(USER_LOGIN));
        user.setPassword(Encryption.decrypt(resultSet.getString(USER_PASSWORD)));
        user.setFirstName(resultSet.getString(USER_FIRST_NAME));
        user.setLastName(resultSet.getString(USER_LAST_NAME));
        user.setPhone(resultSet.getLong(USER_PHONE));
        user.setEmail(resultSet.getString(USER_EMAIL));
        user.setPersonalAccount(resultSet.getInt(USER_PERSONAL_ACCOUNT));
        user.setAdmin(resultSet.getBoolean(USER_IS_ADMIN));
        String authUserSessionId = AuthService.getInstance().getAuthUserSessionId();
        user.setSecretKey(Encryption.encrypt(id + authUserSessionId));

        return user;
    }

    private void setUserToPreparedStatement(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, Encryption.encrypt(user.getPassword()));
        preparedStatement.setString(3, user.getFirstName());

        if (!user.getLastName().equals(EMPTY_STRING)) {
            preparedStatement.setString(4, user.getLastName());
        } else {
            preparedStatement.setNull(4, Types.VARCHAR);
        }

        if (!user.getPhone().equals(LONG_ZERO)) {
            preparedStatement.setLong(5, user.getPhone());
        } else {
            preparedStatement.setNull(5, Types.BIGINT);
        }

        if (!user.getEmail().equals(EMPTY_STRING)) {
            preparedStatement.setString(6, user.getEmail());
        } else {
            preparedStatement.setNull(6, Types.VARCHAR);
        }

        if (!user.getPersonalAccount().equals(0)) {
            preparedStatement.setInt(7, user.getPersonalAccount());
        } else {
            preparedStatement.setNull(7, Types.INTEGER);
        }

        if (!user.getId().equals(LONG_ZERO)) {
            preparedStatement.setLong(8, user.getId());
        }
    }

    private void setRegisteredUserToPreparedStatement(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, Encryption.encrypt(user.getPassword()));

        if (!user.getPhone().equals(LONG_ZERO)) {
            preparedStatement.setLong(3, user.getPhone());
        } else {
            preparedStatement.setNull(3, Types.BIGINT);
        }

        if (!user.getEmail().equals(EMPTY_STRING)) {
            preparedStatement.setString(4, user.getEmail());
        } else {
            preparedStatement.setNull(4, Types.VARCHAR);
        }

        if (!user.getId().equals(LONG_ZERO)) {
            preparedStatement.setLong(5, user.getId());
        }
    }
}
