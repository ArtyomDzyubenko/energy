package com.epam.energy.dao;

import com.epam.energy.exception.DAOException;
import com.epam.energy.model.Meter;
import com.epam.energy.model.Resource;
import com.epam.energy.service.AuthService;
import com.epam.energy.util.Encryption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static com.epam.energy.util.Constants.*;
import static com.epam.energy.util.Constants.LONG_ZERO;

public abstract class AbstractMeterDAO extends AbstractDAO {
    AbstractMeterDAO() throws DAOException {}

    public abstract List<Meter> getMetersByAddressId(Long id) throws DAOException;
    public abstract List<Meter> getMeter(Long id) throws DAOException;
    public abstract void addMeterByAddressId(Meter meter) throws DAOException;
    public abstract void editMeter(Meter meter) throws DAOException;
    public abstract void deleteMeter(Long id) throws DAOException;

    List<Meter> getMeters(Long id, String query) throws DAOException {
        List<Meter> meters = new ArrayList<>();
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                meters.add(getMeterFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return meters;
    }

    void addOrEditMeter(Meter meter, String query) throws DAOException {
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setMeterToPreparedStatement(meter, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.getExceptionMessage(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    private Meter getMeterFromResultSet(ResultSet resultSet) throws SQLException, DAOException {
        Meter meter = new Meter();
        Long meterId = resultSet.getLong(ID);
        meter.setId(meterId);
        meter.setNumber(resultSet.getInt(METER_NUMBER));
        meter.setMeterReaderId(resultSet.getLong(METER_READER_ID));
        meter.setMeterReaderNumber(resultSet.getInt(METER_READER_NUMBER));
        AbstractResourceDAO resourceDAO = ResourceDAO.getInstance();
        List<Resource> resources = resourceDAO.getResourcesByMeterId(meterId);

        if (!resources.isEmpty()) {
            meter.setResource(resources.get(0));
        } else{
            meter.setResource(new Resource());
        }

        String authUserSessionId = AuthService.getInstance().getAuthUserSessionId();
        meter.setAddressId(resultSet.getLong(ADDRESS_ID));
        meter.setSecretKey(Encryption.encrypt(meterId.toString() + authUserSessionId));

        return meter;
    }

    private void setMeterToPreparedStatement(Meter meter, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, meter.getNumber());
        preparedStatement.setLong(2, meter.getResource().getId());

        if (!meter.getMeterReaderId().equals(LONG_ZERO)) {
            preparedStatement.setLong(3, meter.getMeterReaderId());
        } else {
            preparedStatement.setNull(3, Types.BIGINT);
        }

        if (meter.getId().equals(LONG_ZERO)) {
            preparedStatement.setLong(4, meter.getAddressId());
        } else {
            preparedStatement.setLong(4, meter.getAddressId());
            preparedStatement.setLong(5, meter.getId());
        }
    }
}
