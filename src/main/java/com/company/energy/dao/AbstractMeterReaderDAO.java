package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.MeterReader;

import java.util.List;

public interface AbstractMeterReaderDAO {
    List<MeterReader> getAll() throws DAOException;
    List<MeterReader> getMeterReaderById(Long id) throws DAOException;
    void addMeterReader(MeterReader meterReader) throws DAOException;
    void editMeterReader(MeterReader meterReader) throws DAOException;
    void deleteMeterReader(Long id) throws DAOException;
}
