package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Resource;
import com.company.energy.util.ConnectionPool;
import com.company.energy.util.Constants;
import com.company.energy.util.IConnectionPool;
import com.company.energy.util.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAO implements AbstractResourceDAO {
    private static final String GET_RESOURCE_BY_METER_ID =
            "select * " +
            "from resources r " +
            "left join meters m on m.resourceId = r.id " +
            "where m.id = ?";
    private static final String GET_RESOURCES =
            "select * " +
            "from resources";
    private static final String GET_RESOURCE_BY_ID =
            "select * from resources where id = ?";
    private static final String INSERT_RESOURCE =
            "insert into resources(name, cost) " +
            "values(?, ?)";
    private static final String UPDATE_RESOURCE =
            "update resources " +
            "set name = ?, cost = ? " +
            "where id = ?";
    private static final String DELETE_RESOURCE =
            "delete from resources where id = ?";

    private static final IConnectionPool pool = ConnectionPool.getInstance();

    private static ResourceDAO instance;

    private ResourceDAO() { }

    public static synchronized ResourceDAO getInstance() {
        if (instance == null) {
            instance = new ResourceDAO();
        }

        return instance;
    }

    @Override
    public List<Resource> getAll() throws DAOException {
        return getResources(null, GET_RESOURCES);
    }

    @Override
    public List<Resource> getResourceById(Long id) throws DAOException {
        return getResources(id, GET_RESOURCE_BY_ID);
    }

    @Override
    public List<Resource> getResourcesByMeterId(Long id) throws DAOException {
        return getResources(id, GET_RESOURCE_BY_METER_ID);
    }

    @Override
    public void addResource(Resource resource) throws DAOException {
        addOrEditResource(resource, INSERT_RESOURCE);
    }

    @Override
    public void editResource(Resource resource) throws DAOException {
        addOrEditResource(resource, UPDATE_RESOURCE);
    }

    @Override
    public void deleteResource(Long id) throws DAOException {
        deleteEntityById(id);
    }

    private void deleteEntityById(Long id) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(DELETE_RESOURCE)) {

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    List<Resource> getResources(Long id, String query) throws DAOException {
        List<Resource> resources = new ArrayList<>();
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            if (id != null) {
                preparedStatement.setLong(1, id);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                resources.add(getResourceFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return resources;
    }

    void addOrEditResource(Resource resource, String query) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            setResourceToPreparedStatement(resource, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
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
