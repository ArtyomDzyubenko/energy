package service;

import exception.DAOException;
import exception.ServiceException;
import exception.ValidationException;
import model.MeterReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import static util.Constants.*;
import static util.ServicesAllowedInputParametersLists.editMeterReaderServiceAllowedInputParameters;

public class EditMeterReaderService extends AbstractService {
    private static EditMeterReaderService instance;

    private EditMeterReaderService() throws DAOException{}

    public static synchronized EditMeterReaderService getInstance() throws DAOException {
        if (instance==null){
            instance = new EditMeterReaderService();
        }

        return instance;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        try {
            Map<String, String[]> parameters = request.getParameterMap();
            parametersValidator.validate(parameters, editMeterReaderServiceAllowedInputParameters);

            Long meterReaderId = getMeterReaderId(parameters);

            MeterReader reader = meterReaderDAO.getMeterReaderById(meterReaderId).get(0);

            request.setAttribute(METER_READER_ATTRIBUTE_NAME, reader);
            request.getRequestDispatcher(METER_READERS_JSP).forward(request, response);
        } catch (ServletException e) {
            throw new ServiceException(e);
        } catch (IOException e) {
            throw new ServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (ValidationException e) {
            throw new ServiceException(e);
        }
    }
}
