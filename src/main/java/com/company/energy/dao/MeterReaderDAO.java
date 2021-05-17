package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.MeterReader;
import com.company.energy.util.ConnectionPool;
import com.company.energy.util.IConnectionPool;
import com.company.energy.util.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.company.energy.util.Constants.*;

public class MeterReaderDAO implements AbstractMeterReaderDAO {
    private static final String GET_METER_READERS =
            "select * from meterReaders";
    private static final String INSERT_METER_READER =
            "insert into meterReaders(readerNumber, IPAddress, port) " +
            "values (?, ?, ?);";
    private static final String UPDATE_METER_READER =
            "update meterReaders " +
            "set readerNumber=?, IPAddress=?, port=? " +
            "where id = ?;";
    private static final String GET_METER_READER_BY_ID =
            "select * " +
            "from meterReaders " +
            "where id = ?";
    private static final String DELETE_METER_READER =
            "delete from meterReaders where id = ?;";

    private static final IConnectionPool pool = ConnectionPool.getInstance();

    private static MeterReaderDAO instance;

    private MeterReaderDAO() {}

    public static synchronized MeterReaderDAO getInstance() {
        if (instance == null) {
            instance = new MeterReaderDAO();
        }

        return instance;
    }

    @Override
    public List<MeterReader> getAll() throws DAOException{
        return getMeterReaders(null, GET_METER_READERS);
    }

    @Override
    public List<MeterReader> getMeterReaderById(Long id) throws DAOException{
        return getMeterReaders(id, GET_METER_READER_BY_ID);
    }

    @Override
    public void addMeterReader(MeterReader meterReader) throws DAOException{
        addOrEditMeterReader(meterReader, INSERT_METER_READER);
    }

    @Override
    public void editMeterReader(MeterReader meterReader) throws DAOException{
        addOrEditMeterReader(meterReader, UPDATE_METER_READER);
    }

    @Override
    public void deleteMeterReader(Long id) throws DAOException{
        deleteEntityById(id);
    }

    private void deleteEntityById(Long id) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(DELETE_METER_READER)) {

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

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
            throw new DAOException(e);
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
