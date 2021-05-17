package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Address;

import java.util.List;

public interface AbstractAddressDAO {
    List<Address> getAddressesByUserId(Long userId) throws DAOException;
    List<Address> getAddressById(Long addressId) throws DAOException;
    List<Address> getAll() throws DAOException;
    void addAddressByUserId(Address address) throws DAOException;
    void editAddress(Address address) throws DAOException;
    void deleteAddressById(Long addressId) throws DAOException;
}
