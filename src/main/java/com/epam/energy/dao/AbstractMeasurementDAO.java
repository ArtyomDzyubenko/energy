package com.epam.energy.dao;

import com.epam.energy.exception.DAOException;
import com.epam.energy.model.Measurement;
import com.epam.energy.model.Meter;
import com.epam.energy.service.AuthService;
import com.epam.energy.util.Encryption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static com.epam.energy.util.Constants.*;
import static com.epam.energy.util.Constants.METER_ID;

public abstract class AbstractMeasurementDAO extends AbstractDAO{
    private static final String GET_METER_BY_METER_NUMBER = "select *\n" +
            "from meters\n" +
            "where number = ?";

    AbstractMeasurementDAO() throws DAOException {}

    public abstract List<Measurement> getMeasurementByMeterId(Long id) throws DAOException;
    public abstract List<Measurement> getMeasurementById(Long id) throws DAOException;
    public abstract void addMeasurement(Measurement measurement) throws DAOException;
    public abstract void editMeasurement(Measurement measurement) throws DAOException;
    public abstract void deleteMeasurementById(Long id) throws DAOException;
    public abstract void addMeasurements(List<Meter> meters) throws DAOException;

    List<Measurement> getMeasurements(Long id, String query) throws DAOException {
        List<Measurement> measurements = new ArrayList<>();
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            String authUserSessionId = AuthService.getInstance().getAuthUserSessionId();

            while (resultSet.next()) {
                Measurement measurement = new Measurement();
                Long measurementId = resultSet.getLong(ID);
                measurement.setId(measurementId);
                measurement.setDateTime(resultSet.getTimestamp(MEASUREMENT_DATE_TIME));
                measurement.setValue(resultSet.getDouble(MEASUREMENT_VALUE));
                measurement.setMeterId(resultSet.getLong(METER_ID));
                measurement.setSecretKey(Encryption.encrypt(measurementId.toString() + authUserSessionId));
                measurements.add(measurement);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return  measurements;
    }

    void addOrEditMeasurement(Measurement measurement, String query) throws DAOException {
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setTimestamp(1, measurement.getDateTime());
            preparedStatement.setDouble(2, measurement.getValue());

            if (!measurement.getMeterId().equals(LONG_ZERO)) {
                preparedStatement.setLong(3, measurement.getMeterId());
            } else {
                preparedStatement.setLong(3, measurement.getId());
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.getExceptionMessage(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    void addMeasurementsFromMeters(List<Meter> meters, String query) throws DAOException{
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (Meter meter : meters) {
                Long meterId = getMeterIdByMeterNumber(meter.getNumber(), connection);

                if (!meterId.equals(LONG_ZERO)) {
                    preparedStatement.setTimestamp(1, meter.getMeasurement().getDateTime());
                    preparedStatement.setDouble(2, meter.getMeasurement().getValue());
                    preparedStatement.setLong(3, meterId);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    private Long getMeterIdByMeterNumber(int meterNumber, Connection connection) throws DAOException {
        try(PreparedStatement preparedStatement = connection.prepareStatement(GET_METER_BY_METER_NUMBER)) {
            preparedStatement.setInt(1, meterNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong(ID);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return LONG_ZERO;
    }
}
