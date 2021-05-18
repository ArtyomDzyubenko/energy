package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Invoice;
import com.company.energy.model.Measurement;
import com.company.energy.model.Meter;
import com.company.energy.util.ConnectionPool;
import com.company.energy.util.Constants;
import com.company.energy.util.IConnectionPool;
import com.company.energy.util.PooledConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO implements AbstractInvoiceDAO {
    private static final String GET_INVOICES_BY_USER_ID =
            "select * " +
            "from invoices i " +
            "where i.userId = ?";
    private static final String INSERT_INVOICE_BY_USER_ID =
            "insert into invoices(date, meterId, startPeriodId, endPeriodId, consumption, price, userId, isPaid) " +
            "values(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_INVOICE_PAY_STATUS =
            "update invoices " +
            "set isPaid = ? " +
            "where id = ?";
    private static final String DELETE_INVOICE =
            "delete from invoices where id = ?";

    private static final IConnectionPool pool = ConnectionPool.getInstance();
    private static final AbstractMeterDAO meterDAO = MeterDAO.getInstance();
    private static final AbstractMeasurementDAO measurementDAO = MeasurementDAO.getInstance();

    private static InvoiceDAO instance;

    private InvoiceDAO() {}

    public static synchronized InvoiceDAO getInstance() {
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
        deleteEntityById(id);
    }

    private void deleteEntityById(Long id) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(DELETE_INVOICE)) {

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    List<Invoice> getInvoices(Long id, String query) throws DAOException {
        List<Invoice> invoices = new ArrayList<>();

        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                invoices.add(getInvoiceFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return invoices;
    }

    void addInvoice(Invoice invoice, String query) throws DAOException{
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            setInvoiceToPreparedStatement(invoice, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    void setPayStatus(Long id, boolean payStatus, String query) throws DAOException {
        try (PooledConnection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {

            preparedStatement.setLong(2, id);
            preparedStatement.setBoolean(1, payStatus);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private Invoice getInvoiceFromResultSet(ResultSet resultSet) throws SQLException, DAOException {
        Invoice invoice = new Invoice();
        Long invoiceId = resultSet.getLong(Constants.ID);
        invoice.setId(invoiceId);
        invoice.setDate(resultSet.getDate(Constants.INVOICE_DATE).toLocalDate());
        List<Meter> meters = meterDAO.getMeterById(resultSet.getLong(Constants.INVOICE_METER_ID));

        if (!meters.isEmpty()) {
            invoice.setMeter(meters.get(0));
        } else {
            invoice.setMeter(new Meter());
        }

        Long startMeasurementId = resultSet.getLong(Constants.INVOICE_START_MEASUREMENT_ID);
        List<Measurement> startMeasurements = measurementDAO.getMeasurementById(startMeasurementId);

        if (!startMeasurements.isEmpty()) {
            invoice.setStartValue(startMeasurements.get(0));
        } else {
            invoice.setStartValue(new Measurement());
        }

        Long endMeasurementId = resultSet.getLong(Constants.INVOICE_END_MEASUREMENT_ID);
        List<Measurement> endMeasurements = measurementDAO.getMeasurementById(endMeasurementId);

        if (!endMeasurements.isEmpty()) {
            invoice.setEndValue(endMeasurements.get(0));
        } else {
            invoice.setEndValue(new Measurement());
        }

        invoice.setConsumption(resultSet.getDouble(Constants.INVOICE_CONSUMPTION));
        invoice.setPrice(resultSet.getDouble(Constants.INVOICE_PRICE));
        invoice.setUserId(resultSet.getLong(Constants.INVOICE_USER_ID));
        invoice.setPaid(resultSet.getBoolean(Constants.INVOICE_IS_PAID));

        return invoice;
    }

    private void setInvoiceToPreparedStatement(Invoice invoice, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setDate(1, Date.valueOf(invoice.getDate()));
        preparedStatement.setLong(2, invoice.getMeter().getId());
        preparedStatement.setLong(3, invoice.getStartValue().getId());
        preparedStatement.setLong(4, invoice.getEndValue().getId());
        preparedStatement.setDouble(5, invoice.getConsumption());
        preparedStatement.setDouble(6, invoice.getPrice());
        preparedStatement.setLong(7, invoice.getUserId());
        preparedStatement.setBoolean(8, invoice.isPaid());
    }
}
