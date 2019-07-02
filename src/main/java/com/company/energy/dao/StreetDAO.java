package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Street;

import java.util.List;

public class StreetDAO extends AbstractStreetDAO {
    private static final String GET_STREET_BY_ADDRESS_ID = "select *\n" +
            "from streets s\n" +
            "left join addresses a on a.streetId = s.id\n" +
            "where a.id = ?;";
    private static final String GET_STREETS = "select *\n" +
            "from streets;";
    private static final String GET_STREET_BY_ID = "select * from streets where id=?;";
    private static final String INSERT_STREET = "insert into streets(name)\n" +
            "values(?);";
    private static final String UPDATE_STREET = "update streets\n" +
            "set name = ?\n" +
            "where id = ?;";
    private static final String DELETE_STREET = "delete from streets where id = ?;";
    private static StreetDAO instance;

    private StreetDAO() throws DAOException {}

    public static synchronized StreetDAO getInstance() throws DAOException {
        if (instance == null) {
            instance = new StreetDAO();
        }

        return instance;
    }

    @Override
    public List<Street> getAll() throws DAOException {
        return getStreets(null, GET_STREETS);
    }

    @Override
    public List<Street> getStreetByAddressId(Long id) throws DAOException {
        return getStreets(id, GET_STREET_BY_ADDRESS_ID);
    }

    @Override
    public List<Street> getStreetById(Long id) throws DAOException {
        return getStreets(id, GET_STREET_BY_ID);
    }

    @Override
    public void addStreet(Street street) throws DAOException {
        addOrEditStreet(street, INSERT_STREET);
    }

    @Override
    public void editStreet(Street street) throws DAOException {
        addOrEditStreet(street, UPDATE_STREET);
    }

    @Override
    public void deleteStreetById(Long id) throws DAOException {
        deleteEntityById(id, DELETE_STREET);
    }
}
