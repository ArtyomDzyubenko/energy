package com.epam.energy.dao;

import com.epam.energy.exception.DAOException;
import com.epam.energy.model.Measurement;
import com.epam.energy.model.Meter;
import java.util.List;

public class MeasurementDAO extends AbstractMeasurementDAO {
    private static final String GET_MEASUREMENTS_BY_METER_ID = "select *\n" +
            "from measurements\n" +
            "where meterId = ?;";
    private static final String GET_MEASUREMENT_BY_ID = "select *\n" +
            "from measurements\n" +
            "where id = ?;";
    private static final String INSERT_MEASUREMENT_BY_METER_ID = "insert into measurements(dateTime, value, meterId)\n" +
            "values(?, ?, ?);";
    private static final String INSERT_MEASUREMENT_BY_METER_NUMBER = "insert into measurements(dateTime, value, meterId)\n" +
            "values(?, ?, ?);";
    private static final String UPDATE_MEASUREMENT = "update measurements\n" +
            "set dateTime = ?, value = ?\n" +
            "where id = ?;";
    private static final String DELETE_MEASUREMENT = "delete from measurements where id=?;";
    private static MeasurementDAO instance;

    private MeasurementDAO() throws DAOException { }

    public static synchronized MeasurementDAO getInstance() throws DAOException {
        if (instance==null){
            instance = new MeasurementDAO();
        }

        return instance;
    }

    @Override
    public List<Measurement> getMeasurementByMeterId(Long id) throws DAOException {
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
        deleteEntityById(id, DELETE_MEASUREMENT);
    }
}
