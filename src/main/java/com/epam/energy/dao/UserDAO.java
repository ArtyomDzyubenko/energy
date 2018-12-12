package com.epam.energy.dao;

import com.epam.energy.exception.DAOException;
import com.epam.energy.model.User;
import java.util.List;
import static com.epam.energy.util.Constants.LONG_ZERO;

public class UserDAO extends AbstractUserDAO {
    private static final String GET_USER_BY_ID = "select *\n" +
            "from users\n" +
            "where id = ?;";
    private static final String GET_USER_BY_LOGIN_AND_PASSWORD ="select *\n" +
            "from users\n" +
            "where login like ? and password like ?;";
    private static final String GET_USERS = "select *\n" +
            "from users\n" +
            "where isAdmin = false;";
    private static final String INSERT_USER = "insert into users(login, password, firstName, lastName, phone, email, " +
            "personalAccount, isAdmin)\n" +
            "values(?, ?, ?, ?, ?, ?, ?, false);";
    private static final String UPDATE_USER = "update users\n" +
            "set login = ?, password = ?, firstName = ?, lastName = ?, phone = ?, email = ?, personalAccount = ?\n" +
            "where id = ?;";
    private static final String INSERT_REGISTERED_USER = "insert into users(login, password, phone, email, isAdmin)\n" +
            "values(?, ?, ?, ?, false);";
    private static final String UPDATE_REGISTERED_USER = "update users\n" +
            "set login = ?, password = ?, phone = ?, email = ?\n" +
            "where id = ?;";
    private static final String DELETE_USER = "delete from users where id = ?;";
    private static UserDAO instance;

    private UserDAO() throws DAOException { }

    public static synchronized UserDAO getInstance() throws DAOException {
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
        deleteEntityById(id, DELETE_USER);
    }


}
