package com.epam.energy.dao;

import com.epam.energy.exception.DAOException;
import com.epam.energy.model.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static com.epam.energy.util.Constants.*;

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

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            if (id != null){
                preparedStatement.setLong(1, id);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Resource resource = new Resource();
                resource.setId(resultSet.getLong(ID));
                resource.setName(resultSet.getString(RESOURCE_NAME));
                resource.setCost(resultSet.getDouble(RESOURCE_COST));
                resources.add(resource);
            }
        } catch (SQLException e){
            throw new DAOException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return resources;
    }

    void addOrEditResource(Resource resource, String query) throws DAOException {
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, resource.getName());
            preparedStatement.setDouble(2, resource.getCost());

            if (!resource.getId().equals(LONG_ZERO)) {
                preparedStatement.setLong(3, resource.getId());
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.getExceptionMessage(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }
}
