package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Invoice;

import java.util.List;

public interface AbstractInvoiceDAO {
    List<Invoice> getInvoicesByUserId(Long id) throws DAOException;
    void addInvoiceByUserId(Invoice invoice) throws DAOException;
    void updatePayStatusById(Long id, boolean status) throws DAOException;
    void deleteInvoiceById(Long id) throws DAOException;
}
