package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.MeterReader;
import com.company.energy.util.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.company.energy.util.Constants.*;

public abstract class AbstractMeterReaderDAO extends AbstractDAO {
    AbstractMeterReaderDAO() throws DAOException { }

    public abstract List<MeterReader> getAll() throws DAOException;
    public abstract List<MeterReader> getMeterReaderById(Long id) throws DAOException;
    public abstract void addMeterReader(MeterReader meterReader) throws DAOException;
    public abstract void editMeterReader(MeterReader meterReader) throws DAOException;
    public abstract void deleteMeterReader(Long id) throws DAOException;

    List<MeterReader> getMeterReaders(Long id, String query) throws DAOException {
        List<MeterReader> meterReaders = new ArrayList<>();

        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            if (id != null) {
                preparedStatement.setLong(1, id);
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                meterReaders.add(getMeterReaderFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return meterReaders;
    }

    void addOrEditMeterReader(MeterReader meterReader, String query) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            setMeterReaderToPreparedStatement(meterReader, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.getExceptionMessage(e);
        }
    }

    private MeterReader getMeterReaderFromResultSet(ResultSet resultSet) throws SQLException {
        MeterReader meterReader = new MeterReader();
        meterReader.setId(resultSet.getLong(ID));
        meterReader.setNumber(resultSet.getInt(METER_READER_NUMBER));
        meterReader.setIPAddress(resultSet.getString(METER_READER_IP_ADDRESS));
        meterReader.setPort(resultSet.getInt(METER_READER_PORT));

        return meterReader;
    }

    private void setMeterReaderToPreparedStatement(MeterReader meterReader, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, meterReader.getNumber());
        preparedStatement.setString(2, meterReader.getIPAddress());
        preparedStatement.setInt(3, meterReader.getPort());

        if (!meterReader.getId().equals(LONG_ZERO)) {
            preparedStatement.setLong(4, meterReader.getId());
        }
    }
}