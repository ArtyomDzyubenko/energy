package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Measurement;
import com.company.energy.model.Meter;
import com.company.energy.service.AuthService;
import com.company.energy.util.Constants;
import com.company.energy.util.Encryption;
import com.company.energy.util.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMeasurementDAO extends AbstractDAO{
    private static final String GET_METER_BY_METER_NUMBER = "select *\n" +
            "from meters\n" +
            "where meters.number = ?";

    AbstractMeasurementDAO() throws DAOException {}

    public abstract List<Measurement> getMeasurementsByMeterId(Long id) throws DAOException;
    public abstract List<Measurement> getMeasurementById(Long id) throws DAOException;
    public abstract void addMeasurement(Measurement measurement) throws DAOException;
    public abstract void addMeasurements(List<Meter> meters) throws DAOException;
    public abstract void editMeasurement(Measurement measurement) throws DAOException;
    public abstract void deleteMeasurementById(Long id) throws DAOException;

    List<Measurement> getMeasurements(Long id, String query) throws DAOException {
        List<Measurement> measurements = new ArrayList<>();

        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                measurements.add(getMeasurementFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return  measurements;
    }

    void addOrEditMeasurement(Measurement measurement, String query) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            setMeasurementToPreparedStatement(measurement, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.getExceptionMessage(e);
        }
    }

    void addMeasurementsFromMeters(List<Meter> meters, String query) throws DAOException{
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            for (Meter meter : meters) {
                Long meterId = getMeterIdByMeterNumber(meter.getNumber());
                meter.setId(meterId);

                if (!meterId.equals(Constants.LONG_ZERO)) {
                    setMeasurementFromMeterToPreparedStatement(meter, preparedStatement);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private Long getMeterIdByMeterNumber(int meterNumber) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(GET_METER_BY_METER_NUMBER)) {

            preparedStatement.setInt(1, meterNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong(Constants.ID);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return Constants.LONG_ZERO;
    }

    private Measurement getMeasurementFromResultSet(ResultSet resultSet) throws DAOException, SQLException {
        Measurement measurement = new Measurement();
        Long measurementId = resultSet.getLong(Constants.ID);
        measurement.setId(measurementId);
        measurement.setDateTime(resultSet.getTimestamp(Constants.MEASUREMENT_DATE_TIME));
        measurement.setValue(resultSet.getDouble(Constants.MEASUREMENT_VALUE));
        measurement.setMeterId(resultSet.getLong(Constants.METER_ID));
        String authorizedUserSessionId = AuthService.getInstance().getAuthorizedUserSessionId();
        measurement.setSecretKey(Encryption.encrypt(measurementId.toString() + authorizedUserSessionId));

        return measurement;
    }

    private void setMeasurementToPreparedStatement(Measurement measurement, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setTimestamp(1, measurement.getDateTime());
        preparedStatement.setDouble(2, measurement.getValue());

        if (!measurement.getMeterId().equals(Constants.LONG_ZERO)) {
            preparedStatement.setLong(3, measurement.getMeterId());
        } else {
            preparedStatement.setLong(3, measurement.getId());
        }
    }

    private void setMeasurementFromMeterToPreparedStatement(Meter meter, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setTimestamp(1, meter.getMeasurement().getDateTime());
        preparedStatement.setDouble(2, meter.getMeasurement().getValue());
        preparedStatement.setLong(3, meter.getId());
    }
}