package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Meter;
import com.company.energy.model.Resource;
import com.company.energy.service.AuthService;
import com.company.energy.util.Constants;
import com.company.energy.util.Encryption;
import com.company.energy.util.PooledConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMeterDAO extends AbstractDAO {
    AbstractMeterDAO() throws DAOException {}

    public abstract List<Meter> getMetersByAddressId(Long id) throws DAOException;
    public abstract List<Meter> getMeterById(Long id) throws DAOException;
    public abstract void addMeterByAddressId(Meter meter) throws DAOException;
    public abstract void editMeter(Meter meter) throws DAOException;
    public abstract void deleteMeter(Long id) throws DAOException;

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
            exceptionHandler.getExceptionMessage(e);
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
