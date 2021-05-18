package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Measurement;
import com.company.energy.model.Meter;
import com.company.energy.service.AuthService;
import com.company.energy.util.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MeasurementDAO implements AbstractMeasurementDAO {
    private static final String GET_MEASUREMENTS_BY_METER_ID =
            "select * " +
            "from measurements " +
            "where meterId = ? ";
    private static final String GET_MEASUREMENT_BY_ID =
            "select * " +
            "from measurements " +
            "where id = ?";
    private static final String INSERT_MEASUREMENT_BY_METER_ID =
            "insert into measurements(dateTime, value, meterId) " +
            "values(?, ?, ?)";
    private static final String INSERT_MEASUREMENT_BY_METER_NUMBER =
            "insert into measurements(dateTime, value, meterId) " +
            "values(?, ?, ?)";
    private static final String UPDATE_MEASUREMENT =
            "update measurements " +
            "set dateTime = ?, value = ? " +
            "where id = ?";
    private static final String DELETE_MEASUREMENT =
            "delete from measurements where id = ?";
    private static final String GET_METER_BY_METER_NUMBER =
            "select * " +
            "from meters " +
            "where meters.number = ?";

    private static final IConnectionPool pool = ConnectionPool.getInstance();

    private static MeasurementDAO instance;

    private MeasurementDAO() {}

    public static synchronized MeasurementDAO getInstance() {
        if (instance == null) {
            instance = new MeasurementDAO();
        }

        return instance;
    }

    @Override
    public List<Measurement> getMeasurementsByMeterId(Long id) throws DAOException {
        return getMeasurements(id, GET_MEASUREMENTS_BY_METER_ID);
    }

    @Override
    public List<Measurement> getMeasurementById(Long id) throws DAOException {
        return getMeasurements(id, GET_MEASUREMENT_BY_ID);
    }

    @Override
    public void addMeasurement(Measurement measurement) throws DAOException {
        addOrEditMeasurement(measurement, INSERT_MEASUREMENT_BY_METER_ID);
    }

    @Override
    public void addMeasurements(List<Meter> meters) throws DAOException {
        addMeasurementsFromMeters(meters, INSERT_MEASUREMENT_BY_METER_NUMBER);
    }

    @Override
    public void editMeasurement(Measurement measurement) throws DAOException {
        addOrEditMeasurement(measurement, UPDATE_MEASUREMENT);
    }

    @Override
    public void deleteMeasurementById(Long id) throws DAOException {
        deleteEntityById(id);
    }

    private void deleteEntityById(Long id) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(DELETE_MEASUREMENT)) {

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

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
            throw new DAOException(e);
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
