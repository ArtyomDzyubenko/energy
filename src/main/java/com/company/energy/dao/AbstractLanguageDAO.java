package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Language;

import java.util.List;

public interface AbstractLanguageDAO {
    List<Language> getAll() throws DAOException;
    List<Language> getLanguageById(Long id) throws DAOException;
}
