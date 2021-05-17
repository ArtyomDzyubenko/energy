package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Resource;

import java.util.List;

public interface AbstractResourceDAO {
    List<Resource> getAll() throws DAOException;
    List<Resource> getResourceById(Long id) throws DAOException;
    List<Resource> getResourcesByMeterId(Long id) throws DAOException;
    void addResource(Resource resource) throws DAOException;
    void editResource(Resource resource) throws DAOException;
    void deleteResource(Long id) throws DAOException;
}
