package com.company.energy.dao;

import com.company.energy.exception.DAOException;
import com.company.energy.model.Invoice;
import com.company.energy.model.Measurement;
import com.company.energy.model.Meter;
import com.company.energy.service.AuthService;
import com.company.energy.util.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInvoiceDAO extends AbstractDAO{
    private AbstractMeterDAO meterDAO = MeterDAO.getInstance();
    private AbstractMeasurementDAO measurementDAO = MeasurementDAO.getInstance();

    AbstractInvoiceDAO() throws DAOException {}

    public abstract List<Invoice> getInvoicesByUserId(Long id) throws DAOException;
    public abstract void addInvoiceByUserId(Invoice invoice) throws DAOException;
    public abstract void updatePayStatusById(Long id, boolean status) throws DAOException;
    public abstract void deleteInvoiceById(Long id) throws DAOException;

    List<Invoice> getInvoices(Long id, String query) throws DAOException {
        List<Invoice> invoices = new ArrayList<>();
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                invoices.add(getInvoiceFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            exceptionHandler.getExceptionMessage(e);
        } finally {
            pool.releaseConnection(connection);
        }

        return invoices;
    }

    void addInvoice(Invoice invoice, String query) throws DAOException{
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            setInvoiceToPreparedStatement(invoice, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.getExceptionMessage(e);
        } finally {
            pool.releaseConnection(connection);
        }
    }

    void setPayStatus(Long id, boolean payStatus, String query) throws DAOException {
        Connection connection = pool.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(2, id);
            preparedStatement.setBoolean(1, payStatus);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            pool.releaseConnection(connection);
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

        String authorizedUserSessionId = AuthService.getInstance().getAuthorizedUserSessionId();
        invoice.setConsumption(resultSet.getDouble(Constants.INVOICE_CONSUMPTION));
        invoice.setPrice(resultSet.getDouble(Constants.INVOICE_PRICE));
        invoice.setUserId(resultSet.getLong(Constants.INVOICE_USER_ID));
        invoice.setPaid(resultSet.getBoolean(Constants.INVOICE_IS_PAID));
        invoice.setSecretKey(invoiceId + authorizedUserSessionId);

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
