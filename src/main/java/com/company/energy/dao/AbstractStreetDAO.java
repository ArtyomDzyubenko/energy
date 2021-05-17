package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Street;

import java.util.List;

public interface AbstractStreetDAO {
    List<Street> getAll() throws DAOException;
    List<Street> getStreetByAddressId(Long id) throws DAOException;
    List<Street> getStreetById(Long id) throws DAOException;
    void addStreet(Street street) throws DAOException;
    void editStreet(Street street) throws DAOException;
    void deleteStreetById(Long id) throws DAOException;
}
