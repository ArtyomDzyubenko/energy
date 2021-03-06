package com.company.energy.service;

import com.company.energy.dao.AbstractMeterReaderDAO;
import com.company.energy.dao.MeterReaderDAO;
import com.company.energy.exception.DAOException;
import com.company.energy.exception.ServiceException;

import java.io.IOException;
import java.util.List;
import com.company.energy.model.MeterReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.company.energy.util.Constants.*;
import static com.company.energy.util.Constants.METER_READERS_JSP;

public class GetMeterReadersService extends AbstractService {
    private static final AbstractMeterReaderDAO meterReaderDAO = MeterReaderDAO.getInstance();

    private static GetMeterReadersService instance;

    private GetMeterReadersService() throws DAOException { }

    public static synchronized GetMeterReadersService getInstance() throws DAOException {
        if (instance == null) {
            instance = new GetMeterReadersService();
        }

        return instance;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            List<MeterReader> readers = meterReaderDAO.getAll();

            saveLastServiceURL(METER_READERS_URL_LAST_STATE, request);
            request.setAttribute(METER_READERS_ATTRIBUTE, readers);
            request.getRequestDispatcher(METER_READERS_JSP).forward(request, response);
        } catch (ServletException | IOException | DAOException e) {
            throw new ServiceException(e);
        }
    }
}
