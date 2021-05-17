package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Meter;

import java.util.List;

public interface AbstractMeterDAO {
    List<Meter> getMetersByAddressId(Long id) throws DAOException;
    List<Meter> getMeterById(Long id) throws DAOException;
    void addMeterByAddressId(Meter meter) throws DAOException;
    void editMeter(Meter meter) throws DAOException;
    void deleteMeter(Long id) throws DAOException;
}
