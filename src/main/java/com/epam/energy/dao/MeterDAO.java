package com.epam.energy.dao;

import com.epam.energy.exception.DAOException;
import com.epam.energy.model.Meter;
import java.util.List;

public class MeterDAO extends AbstractMeterDAO {
    private static final String GET_METERS_BY_ADDRESS_ID = "select *\n" +
            "from meters m\n" +
            "left join addresses a on a.id = m.addressId\n" +
            "left join meterReaders mr on mr.id = m.meterReaderId\n" +
            "where a.id = ?";
    private static final String GET_METER_BY_ID = "select *\n" +
            "from meters m\n" +
            "left join meterReaders mr on mr.id = m.meterReaderId\n" +
            "where m.id = ?";
    private static final String INSERT_METER_BY_ADDRESS_ID = "insert into meters(number, resourceId, meterReaderId, addressId)\n" +
            "values(?, ?, ?, ?);";
    private static final String UPDATE_METER_BY_ID = "update meters\n" +
            "set number = ?, resourceId = ?, meterReaderId = ?, addressId=?\n" +
            "where id = ?;";
    private static final String DELETE_METER = "delete from meters where id = ?;";
    private static MeterDAO instance;

    private MeterDAO() throws DAOException {}

    public static synchronized MeterDAO getInstance() throws DAOException {
        if (instance==null){
            instance = new MeterDAO();
        }

        return instance;
    }

    @Override
    public List<Meter> getMetersByAddressId(Long id) throws DAOException {
        return getMeters(id, GET_METERS_BY_ADDRESS_ID);
    }

    @Override
    public List<Meter> getMeter(Long id) throws DAOException {
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
        deleteEntityById(id, DELETE_METER);
    }
}
