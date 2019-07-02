package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Invoice;

import java.util.List;

public class InvoiceDAO extends AbstractInvoiceDAO {
    private static final String GET_INVOICES_BY_USER_ID = "select *\n" +
            "from invoices i\n" +
            "where i.userId = ?;";
    private static final String INSERT_INVOICE_BY_USER_ID = "insert into invoices(date, meterId, startPeriodId, endPeriodId, " +
            "consumption, price, userId, isPaid)\n" +
            "values(?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_INVOICE_PAY_STATUS =  "update invoices\n" +
            "set isPaid = ?\n" +
            "where id = ?";
    private static final String DELETE_INVOICE = "delete from invoices where id = ?;";
    private static InvoiceDAO instance;

    private InvoiceDAO() throws DAOException {}

    public static synchronized InvoiceDAO getInstance() throws DAOException {
        if (instance == null) {
            instance = new InvoiceDAO();
        }

        return instance;
    }

    @Override
    public List<Invoice> getInvoicesByUserId(Long id) throws DAOException {
        return getInvoices(id, GET_INVOICES_BY_USER_ID);
    }

    @Override
    public void addInvoiceByUserId(Invoice invoice) throws DAOException {
        addInvoice(invoice, INSERT_INVOICE_BY_USER_ID);
    }

    @Override
    public void updatePayStatusById(Long id, boolean payStatus) throws DAOException {
        setPayStatus(id, payStatus, UPDATE_INVOICE_PAY_STATUS);
    }

    @Override
    public void deleteInvoiceById(Long id) throws DAOException{
        deleteEntityById(id, DELETE_INVOICE);
    }
}
