package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Measurement;
import com.company.energy.model.Meter;

import java.util.List;

public interface AbstractMeasurementDAO {
    List<Measurement> getMeasurementsByMeterId(Long id) throws DAOException;
    List<Measurement> getMeasurementById(Long id) throws DAOException;
    void addMeasurement(Measurement measurement) throws DAOException;
    void addMeasurements(List<Meter> meters) throws DAOException;
    void editMeasurement(Measurement measurement) throws DAOException;
    void deleteMeasurementById(Long id) throws DAOException;
}
