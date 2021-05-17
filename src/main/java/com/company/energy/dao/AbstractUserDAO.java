package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.User;

import java.util.List;

public interface AbstractUserDAO {
    List<User> getAll() throws DAOException;
    List<User> getUserById(Long id) throws DAOException;
    User getUserByLoginAndPassword(String login, String password) throws DAOException;
    void addUser(User user) throws DAOException;
    void editUser(User user) throws DAOException;
    void registerUser(User user) throws DAOException;
    void updateRegisteredUser(User user) throws DAOException;
    void deleteUserById(Long id) throws DAOException;
}
