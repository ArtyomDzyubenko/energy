package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Meter;
import com.company.energy.model.Resource;
import com.company.energy.service.AuthService;
import com.company.energy.util.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class MeterDAO implements AbstractMeterDAO {
    private static final String GET_METERS_BY_ADDRESS_ID = 
            "select * " +
            "from meters m " +
            "left join addresses a on a.id = m.addressId " +
            "left join meterReaders mr on mr.id = m.meterReaderId " +
            "where a.id = ?";
    private static final String GET_METER_BY_ID = "select * " +
            "from meters m " +
            "left join meterReaders mr on mr.id = m.meterReaderId " +
            "where m.id = ?";
    private static final String INSERT_METER_BY_ADDRESS_ID =
            "insert into meters(number, resourceId, meterReaderId, addressId) " +
            "values(?, ?, ?, ?)";
    private static final String UPDATE_METER_BY_ID =
            "update meters " +
            "set number = ?, resourceId = ?, meterReaderId = ?, addressId=? " +
            "where id = ?";
    private static final String DELETE_METER =
            "delete from meters where id = ?";

    private static final IConnectionPool pool = ConnectionPool.getInstance();

    private static MeterDAO instance;

    private MeterDAO() {}

    public static synchronized MeterDAO getInstance() {
        if (instance == null) {
            instance = new MeterDAO();
        }

        return instance;
    }

    @Override
    public List<Meter> getMetersByAddressId(Long id) throws DAOException {
        return getMeters(id, GET_METERS_BY_ADDRESS_ID);
    }

    @Override
    public List<Meter> getMeterById(Long id) throws DAOException {
        return getMeters(id, GET_METER_BY_ID);
    }

    @Override
    public void addMeterByAddressId(Meter meter) throws DAOException {
        addOrEditMeter(meter, INSERT_METER_BY_ADDRESS_ID);
    }

    @Override
    public void editMeter(Meter meter) throws DAOException {
        addOrEditMeter(meter, UPDATE_METER_BY_ID);
    }

    @Override
    public void deleteMeter(Long id) throws DAOException {
        deleteEntityById(id);
    }

    private void deleteEntityById(Long id) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(DELETE_METER)) {

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    List<Meter> getMeters(Long id, String query) throws DAOException {
        List<Meter> meters = new ArrayList<>();

        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                meters.add(getMeterFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return meters;
    }

    void addOrEditMeter(Meter meter, String query) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            setMeterToPreparedStatement(meter, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private Meter getMeterFromResultSet(ResultSet resultSet) throws SQLException, DAOException {
        Meter meter = new Meter();
        Long meterId = resultSet.getLong(Constants.ID);
        meter.setId(meterId);
        meter.setNumber(resultSet.getInt(Constants.METER_NUMBER));
        meter.setMeterReaderId(resultSet.getLong(Constants.METER_READER_ID));
        meter.setMeterReaderNumber(resultSet.getInt(Constants.METER_READER_NUMBER));
        AbstractResourceDAO resourceDAO = ResourceDAO.getInstance();
        List<Resource> resources = resourceDAO.getResourcesByMeterId(meterId);

        if (!resources.isEmpty()) {
            meter.setResource(resources.get(0));
        } else{
            meter.setResource(new Resource());
        }

        String authorizedUserSessionId = AuthService.getInstance().getAuthorizedUserSessionId();
        meter.setAddressId(resultSet.getLong(Constants.ADDRESS_ID));
        meter.setSecretKey(Encryption.encrypt(meterId.toString() + authorizedUserSessionId));

        return meter;
    }

    private void setMeterToPreparedStatement(Meter meter, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, meter.getNumber());
        preparedStatement.setLong(2, meter.getResource().getId());

        if (!meter.getMeterReaderId().equals(Constants.LONG_ZERO)) {
            preparedStatement.setLong(3, meter.getMeterReaderId());
        } else {
            preparedStatement.setNull(3, Types.BIGINT);
        }

        if (meter.getId().equals(Constants.LONG_ZERO)) {
            preparedStatement.setLong(4, meter.getAddressId());
        } else {
            preparedStatement.setLong(4, meter.getAddressId());
            preparedStatement.setLong(5, meter.getId());
        }
    }
}
