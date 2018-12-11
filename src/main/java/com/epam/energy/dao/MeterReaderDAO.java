package com.epam.energy.dao;

import com.epam.energy.exception.DAOException;
import com.epam.energy.model.MeterReader;
import java.util.List;

public class MeterReaderDAO extends AbstractMeterReaderDAO {
    private static final String GET_METER_READERS = "select * from meterReaders;";
    private static final String INSERT_METER_READER = "insert into meterReaders(readerNumber, IPAddress, port)\n" +
            "values (?, ?, ?);";
    private static final String UPDATE_METER_READER = "update meterReaders\n" +
            "set readerNumber=?, IPAddress=?, port=?\n" +
            "where id = ?;";
    private static final String GET_METER_READER_BY_ID = "select *\n" +
            "from meterReaders\n" +
            "where id = ?";
    private static final String DELETE_METER_READER = "delete from meterReaders where id = ?;";
    private static MeterReaderDAO instance;

    private MeterReaderDAO() throws DAOException {}

    public static synchronized MeterReaderDAO getInstance() throws DAOException {
        if (instance==null){
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
        deleteEntityById(id, DELETE_METER_READER);
    }
}
