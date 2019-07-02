package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Resource;
import com.company.energy.util.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractResourceDAO extends AbstractDAO {
    AbstractResourceDAO() throws DAOException {}

    public abstract List<Resource> getAll() throws DAOException;
    public abstract List<Resource> getResourceById(Long id) throws DAOException;
    public abstract List<Resource> getResourcesByMeterId(Long id) throws DAOException;
    public abstract void addResource(Resource resource) throws DAOException;
    public abstract void editResource(Resource resource) throws DAOException;
    public abstract void deleteResource(Long id) throws DAOException;

    List<Resource> getResources(Long id, String query) throws DAOException {
        List<Resource> resources = new ArrayList<>();
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (id != null) {
                preparedStatement.setLong(1, id);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                resources.add(getResourceFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return resources;
    }

    void addOrEditResource(Resource resource, String query) throws DAOException {
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setResourceToPreparedStatement(resource, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.getExceptionMessage(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    private Resource getResourceFromResultSet(ResultSet resultSet) throws SQLException {
        Resource resource = new Resource();
        resource.setId(resultSet.getLong(Constants.ID));
        resource.setName(resultSet.getString(Constants.RESOURCE_NAME));
        resource.setCost(resultSet.getDouble(Constants.RESOURCE_COST));

        return resource;
    }

    private void setResourceToPreparedStatement(Resource resource, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, resource.getName());
        preparedStatement.setDouble(2, resource.getCost());

        if (!resource.getId().equals(Constants.LONG_ZERO)) {
            preparedStatement.setLong(3, resource.getId());
        }
    }
}
