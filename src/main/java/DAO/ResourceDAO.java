package DAO;

import exception.DAOException;
import model.Resource;
import java.util.List;

public class ResourceDAO extends AbstractResourceDAO {
    private static final String GET_RESOURCE_BY_METER_ID = "select *\n" +
            "from resources r\n" +
            "left join meters m on m.resourceId = r.id\n" +
            "where m.id = ?;";
    private static final String GET_RESOURCES = "select *\n" +
            "from resources;";
    private static final String GET_RESOURCE_BY_ID = "select * from resources where id = ?;";
    private static final String INSERT_RESOURCE = "insert into resources(name, cost)\n" +
            "values(?, ?);";
    private static final String UPDATE_RESOURCE = "update resources\n" +
            "set name = ?, cost = ?\n" +
            "where id = ?;";
    private static final String DELETE_RESOURCE = "delete from resources where id = ?;";
    private static ResourceDAO instance;

    private ResourceDAO() throws DAOException { }

    public static synchronized ResourceDAO getInstance() throws DAOException {
        if (instance==null){
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
        deleteEntityById(id, DELETE_RESOURCE);
    }
}
